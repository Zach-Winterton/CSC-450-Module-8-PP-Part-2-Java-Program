//============================================================================
// Name        : JavaConcurrency.java
// Author      : Zach Winterton
// Date        : 02/11/2024
// Description : Java multi-threaded program where one thread counts up from 0 to 20 and the other thread counts down from 20 to 0.
// The counting down only starts after the counting up is finished. This synchronization is achieved using a lock and condition variable.
//============================================================================

//Define package name
package application;

// Declare public class named JavaConcurrency
public class JavaConcurrency {

	// Create lock object for synchronization between threads
	private static final Object lock = new Object();
	// Boolean variable set to false, acts as flag to track if counting up is done
	private static boolean isCountingUpDone = false;
	
	// Main method, entry point for program
	public static void main(String[] args) {
		// Define runnable for counting up
		Runnable countUp = () -> {
			// For loop that iterates from 0 up to 20
			for(int i = 0; i <= 20; i++) {
				// Enter synchronized block to ensure mutual exclusion using lock object
				synchronized (lock) {
					// Print current count of the first thread
					System.out.println("Thread 1: " + i);
					// Check if the count has reached 20 yet
					if (i == 20) {
						// If true, set boolean variable 'isCountingUpDone' to true, indicating counting up is done
						isCountingUpDone = true;
						// Notify waiting thread that condition has changed
						lock.notifyAll();
						// Print completion message once loop has reached 20
						System.out.println("\nThread 1 complete\n");
					}
				}
				try {
					// Sleep thread to slow down thread counting for 400 milliseconds for each iteration
					Thread.sleep(400); 
				} 
				catch (InterruptedException e) {
					// Re-interrupt thread if it was interrupted during sleep
					Thread.currentThread().interrupt();
				}
			}
		};
		
		// Define runnable for counting down
		Runnable countDown = () -> {
			// Enter synchronized block to ensure mutual exclusion
			synchronized (lock) {
				// Waits while counting up is not done
				while (!isCountingUpDone) {
					try {
						// Wait for other thread to finish
						lock.wait();
					}
					catch (InterruptedException e) {
						// Re-interrupt thread if it was interrupted during wait
						Thread.currentThread().interrupt();
					}
				}
			}
			// For loop that iterates from 20 down to 0
			for (int i = 20; i >= 0; i--) {
				// Print current count of the second thread
				System.out.println("thread 2: " + i);
				try {
					// Sleep thread to slow down thread counting for 400 milliseconds for each iteration
					Thread.sleep(400);
				}
				catch (InterruptedException e) {
					// Re-interrupt thread if it was interrupted during sleep
					Thread.currentThread().interrupt();
				}
			}
			// Print completion message once loop is finished
			System.out.println("\nThread 2 complete");
		};
		
		// Create and start new thread for 'countUp'
		new Thread(countUp).start();
		// Create and start new thread for 'countDown'
		new Thread(countDown).start();
	}
}
