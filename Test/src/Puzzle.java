import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Puzzle {
	class Move implements Comparable<Move>{
		int from,hole,to;
		
		Move(int from,int hole,int to){
			this.from = from;
			this.hole = hole;
			this.to = to;
		}
		
		// we need compareTo to sort the arrayList
		@Override
		public int compareTo(Move m) {

			return Integer.valueOf(this.from).compareTo(Integer.valueOf(m.from)) ;
		}
		
	}
	ArrayList<ArrayList<Integer>> board = new ArrayList<ArrayList<Integer>>();
	ArrayList<Move> movesList = new ArrayList<Move>() ;
	ArrayList<ArrayList<ArrayList<Integer>>> unsuccessfulGrid = new ArrayList<ArrayList<ArrayList<Integer>>>() ;
	int move = 0;
	

	Puzzle(ArrayList<ArrayList<Integer>> board){
		this.board = board;
	
	}
	
	Puzzle(){
		// create a board
		Integer[] l1 = {-1,-1,1,1,1,-1,-1};
		Integer[] l2 = {-1,-1,1,1,1,-1,-1};
		Integer[] l3 = {1,1,1,1,1,1,1};
		Integer[] l4 = {1,1,1,0,1,1,1};
		Integer[] l5 = {1,1,1,1,1,1,1};
		Integer[] l6 = {-1,-1,1,1,1,-1,-1};
		Integer[] l7 = {-1,-1,1,1,1,-1,-1};
		
		board.add(new ArrayList<Integer>(Arrays.asList(l1)));
		board.add(new ArrayList<Integer>(Arrays.asList(l2)));
		board.add(new ArrayList<Integer>(Arrays.asList(l3)));
		board.add(new ArrayList<Integer>(Arrays.asList(l4)));
		board.add(new ArrayList<Integer>(Arrays.asList(l5)));
		board.add(new ArrayList<Integer>(Arrays.asList(l6)));
		board.add(new ArrayList<Integer>(Arrays.asList(l7)));
		new Puzzle(board);
	}
	public void print() {
		for(ArrayList<Integer> line: board) {
			for(int i: line) {
				if(i == -1) {
					System.out.print(" ");
				}
				else {
					if(i == 1)
						System.out.print("X");
					else
						System.out.print("O");
				}
				
			}
			System.out.println();
		}
		System.out.println("Trail = " + move);
	}

	public void makeMove(Move move) {
		board.get((move.from)/7).set((move.from)%7, 0);
		board.get((move.hole)/7).set((move.hole)%7, 0);
		board.get((move.to)/7).set((move.to)%7, 1);
		movesList.add(move);
	}
	public void undoMove(Move move) {
		board.get((move.from)/7).set((move.from)%7, 1);
		board.get((move.hole)/7).set((move.hole)%7, 1);
		board.get((move.to)/7).set((move.to)%7, 0);
		movesList.remove(movesList.size()-1);
	}
	public ArrayList<Move> solve() {
		ArrayList<Move> p = new ArrayList<>();
		for(int i = 0; i < board.size(); i++) {
			for(int j = 0; j < board.get(i).size(); j++) {
				if(board.get(i).get(j) == 0) {
					if(i - 2 >= 0) {
						if((board.get(i-2).get(j) == 1) && (board.get(i-1).get(j) ==1)) 
							p.add(new Move((i-2)*7+j,(i-1)*7+j, i *7+j));
						
					}
					if(j-2>=0) {
						if((board.get(i).get(j-2) == 1) && (board.get(i).get(j-1) ==1)) 
							p.add(new Move((i*7)+j-2,(i*7)+j-1, (i *7)+j));
					}
					if(i+2 <= 6) {
						if((board.get(i+2).get(j)==1) && (board.get(i+1).get(j) ==1)){
							p.add(new Move((i+2)*7+j,(i+1)*7+j, i *7+j));
						}
					}
					if(j+2<=6) {
						if((board.get(i).get(j+2) == 1) && (board.get(i).get(j+1) ==1)) 
							p.add(new Move((i*7)+j+2,(i*7)+j+1, (i *7)+j));
					}
				}
				
			}
		}
		return p;
	}
	public int pegCount() {
		int peg = 0;
		for(int i = 0; i < board.size(); i++) {
			for(int j = 0; j < board.get(i).size(); j++) {
				if(board.get(i).get(j) == 1) {
					peg++;
				}
			}
		}
		return peg;
	}
	public ArrayList<ArrayList<Integer>> deepCopy(ArrayList<ArrayList<Integer>> input){
		ArrayList<ArrayList<Integer>> newGrid = new ArrayList<>();
		for(ArrayList<Integer> line : input) {
			ArrayList<Integer> newLine = new ArrayList<>();
			for(Integer i :  line) {
				newLine.add(Integer.valueOf(i));
			}
			newGrid.add(newLine);
		}
		return newGrid;
		
	}
	
	public boolean findSolution(int move) {
		
		if(unsuccessfulGrid.contains(board)) {
			return false;
		}
		if(pegCount() == 1 && board.get(3).get(3)== 1) {
			return true;
		}
		
		else {
			
			ArrayList<Move> moves = solve();
			
			//we sort to make the algorithm work faster
			Collections.sort(moves);
			for(Move mo: moves) {
				makeMove(mo);
				
				if(findSolution(move+1)) {
					
					return true;
				}
				else {
					undoMove(mo);
				}
				this.move++;
				print();	
			}
		
		}
		
		if(!unsuccessfulGrid.contains(board)) {
			unsuccessfulGrid.add(deepCopy(board));
		}
		return false;
	}
	
	public static void main(String args[]) {
		
		Puzzle p = new Puzzle();
		//prints inital board
		p.print();
		//solves
		p.findSolution(1);
		// prints final board
		p.print();
		
		
	}
}

