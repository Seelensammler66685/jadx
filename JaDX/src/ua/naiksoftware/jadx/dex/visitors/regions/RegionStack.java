package jadx.dex.visitors.regions;

import android.util.Log;
import jadx.dex.nodes.BlockNode;
import jadx.dex.nodes.IRegion;
import jadx.dex.nodes.MethodNode;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class RegionStack {
	private static final String tag = "RegionStack";
	private static final boolean DEBUG = false;

	static {
		if (DEBUG) {
                Log.d(tag, "Debug enabled for " + RegionStack.class);
            }
	}

	private static class State {
		final Set<BlockNode> exits;
		IRegion region;

		public State() {
			exits = new HashSet<BlockNode>();
		}

		private State(State c) {
			exits = new HashSet<BlockNode>(c.exits);
		}

		public State copy() {
			return new State(this);
		}

		@Override
		public String toString() {
			return "Exits: " + exits;
		}
	}

	private final Stack<State> stack;
	private State curState;

	public RegionStack(MethodNode mth) {
		if (DEBUG) {
                Log.d(tag, "New RegionStack: {}" + mth);
            }
		this.stack = new Stack<RegionStack.State>();
		this.curState = new State();
	}

	public void push(IRegion region) {
		stack.push(curState);
		if (stack.size() > 1000) {
                throw new StackOverflowError("Deep code hierarchy");
            }

		curState = curState.copy();
		curState.region = region;
		if (DEBUG) {
			Log.d(tag, "Stack push: {} = {}" + region + ", " + curState);
			Log.d(tag, "Stack size: {}" + size());
		}
	}

	public void pop() {
		curState = stack.pop();
		if (DEBUG) {
                Log.d(tag, "Stack pop : {}" + curState);
            }
	}

	/**
	 * Add boundary(exit) node for current stack frame
	 * 
	 * @param exit
	 *            boundary node, null will be ignored
	 */
	public void addExit(BlockNode exit) {
		if (exit != null)
			curState.exits.add(exit);
	}

	public boolean containsExit(BlockNode exit) {
		return curState.exits.contains(exit);
	}

	public IRegion peekRegion() {
		return curState.region;
	}

	public int size() {
		return stack.size();
	}

	@Override
	public String toString() {
		return "Region stack size: " + size() + ", last: " + curState;
	}
}
