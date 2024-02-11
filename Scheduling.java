import java.util.*;
import java.io.*;

enum ProcessState { READY, RUNNING, BLOCKED, TERMINATED }

class PCB {
    int process_id;
    int arrival_time;
    int burst_time;
    int priority;
    int waiting_time;
    int turnaround_time;
    int response_time;
    ProcessState state;

    // Constructor
    public PCB(int pid, int at, int bt, int pr) {
        process_id = pid;
        arrival_time = at;
        burst_time = bt;
        priority = pr;
        waiting_time = 0;
        turnaround_time = 0;
        response_time = 0;
        state = ProcessState.READY;
    }
}

class SJFComparator implements Comparator<PCB> {
    public int compare(PCB p1, PCB p2) {
        return p1.burst_time - p2.burst_time;
    }
}

class PriorityComparator implements Comparator<PCB> {
    public int compare(PCB p1, PCB p2) {
        return p2.priority - p1.priority;
    }
}

public class Scheduling {
    public static void inputProcesses(List<PCB> processes) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int numProcesses = scanner.nextInt();

        for (int i = 0; i < numProcesses; ++i) {
            System.out.print("Enter arrival time for process " + i + ": ");
            int arrivalTime = scanner.nextInt();

            System.out.print("Enter burst time for process " + i + ": ");
            int burstTime = scanner.nextInt();

            System.out.print("Enter priority for process " + i + ": ");
            int priority = scanner.nextInt();

            processes.add(new PCB(i, arrivalTime, burstTime, priority));
        }

        scanner.close();
    }

    // First Come First Serve (FCFS) scheduling
    public static void fcfsScheduling(List<PCB> processes) {
        int currentTime = 0;

        for (PCB process : processes) {
            if (process.arrival_time > currentTime) {
                currentTime = process.arrival_time;
            }

            process.waiting_time = currentTime - process.arrival_time;
            process.turnaround_time = process.waiting_time + process.burst_time;
            process.response_time = process.waiting_time;
            currentTime += process.burst_time;
            process.state = ProcessState.TERMINATED;
        }
    }

    // Shortest Job First (SJF) scheduling
    public static void sjfScheduling(List<PCB> processes) {
        PriorityQueue<PCB> readyQueue = new PriorityQueue<>(new SJFComparator());
        int currentTime = 0;

        while (!readyQueue.isEmpty() || currentTime < processes.get(processes.size() - 1).arrival_time) {
            for (PCB process : processes) {
                if (process.arrival_time == currentTime) {
                    readyQueue.offer(process);
                }
            }

            if (!readyQueue.isEmpty()) {
                PCB currentProcess = readyQueue.poll();
                currentProcess.waiting_time = currentTime - currentProcess.arrival_time;
                currentProcess.turnaround_time = currentProcess.waiting_time + currentProcess.burst_time;
                currentProcess.response_time = currentProcess.waiting_time;
                currentTime += currentProcess.burst_time;
                currentProcess.state = ProcessState.TERMINATED;
            } else {
                currentTime++;
            }
        }
    }

    // Function to simulate Priority scheduling
    public static void priorityScheduling(List<PCB> processes) {
        PriorityQueue<PCB> readyQueue = new PriorityQueue<>(new PriorityComparator());
        int currentTime = 0;

        while (!readyQueue.isEmpty() || currentTime < processes.get(processes.size() - 1).arrival_time) {
            for (PCB process : processes) {
                if (process.arrival_time == currentTime) {
                    readyQueue.offer(process);
                }
            }

            if (!readyQueue.isEmpty()) {
                PCB currentProcess = readyQueue.poll();
                currentProcess.waiting_time = currentTime - currentProcess.arrival_time;
                currentProcess.turnaround_time = currentProcess.waiting_time + currentProcess.burst_time;
                currentProcess.response_time = currentProcess.waiting_time;
                currentTime += currentProcess.burst_time;
                currentProcess.state = ProcessState.TERMINATED;
            } else {
                currentTime++;
            }
        }
    }

    public static double calculateCPUUtilization(List<PCB> processes, int totalTime) {
        return (double) processes.size() / totalTime;
    }

    public static double calculateThroughput(List<PCB> processes, int totalTime) {
        return processes.size() / (double) totalTime;
    }

    public static double calculateAverageWaitingTime(List<PCB> processes) {
        double totalWaitingTime = 0;
        for (PCB process : processes) {
            totalWaitingTime += process.waiting_time;
        }
        return totalWaitingTime / processes.size();
    }

    public static double calculateAverageTurnaroundTime(List<PCB> processes) {
        double totalTurnaroundTime = 0;
        for (PCB process : processes) {
            totalTurnaroundTime += process.turnaround_time;
        }
        return totalTurnaroundTime / processes.size();
    }

    public static void printAnalysis(String algorithm, double avgWaitingTime, double avgTurnaroundTime, double cpuUtilization, double throughput) {
        System.out.println("Scheduling Algorithm: " + algorithm);
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
        System.out.println("CPU Utilization: " + cpuUtilization + "%");
        System.out.println("Throughput: " + throughput);
    }

    public static void writeAnalysisToFile(String filename, String algorithm, double avgWaitingTime, double avgTurnaroundTime, double cpuUtilization, double throughput) {
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write("Scheduling Algorithm: " + algorithm + "\n");
            writer.write("Average Waiting Time: " + avgWaitingTime + "\n");
            writer.write("Average Turnaround Time: " + avgTurnaroundTime + "\n");
            writer.write("CPU Utilization: " + cpuUtilization + "%\n");
            writer.write("Throughput: " + throughput + "\n\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        List<PCB> processes = new ArrayList<>();
        Random rand = new Random();

        try {
            FileWriter writer = new FileWriter("analysis.txt");
            writer.write(""); // Clearing the contents of the file
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while clearing the file.");
            e.printStackTrace();
        }

        for (int numProcesses : new int[] {1, 10, 100, 1000, 10000, 100000}) {
            for (int i = 0; i < numProcesses; ++i) {
                int arrivalTime = rand.nextInt(100);
                int burstTime = rand.nextInt(20) + 1; // Ensure burst time is at least 1
                int priority = rand.nextInt(10);
                processes.add(new PCB(i, arrivalTime, burstTime, priority));
            }

            fcfsScheduling(processes);
            double avgWaitingTimeFCFS = calculateAverageWaitingTime(processes);
            double avgTurnaroundTimeFCFS = calculateAverageTurnaroundTime(processes);
            double cpuUtilizationFCFS = calculateCPUUtilization(processes, processes.get(processes.size() - 1).burst_time);
            double throughputFCFS = calculateThroughput(processes, processes.get(processes.size() - 1).burst_time);
            writeAnalysisToFile("analysis.txt", "First Come First Serve (FCFS) Scheduling", avgWaitingTimeFCFS, avgTurnaroundTimeFCFS, cpuUtilizationFCFS, throughputFCFS);

            sjfScheduling(processes);
            double avgWaitingTimeSJF = calculateAverageWaitingTime(processes);
            double avgTurnaroundTimeSJF = calculateAverageTurnaroundTime(processes);
            double cpuUtilizationSJF = calculateCPUUtilization(processes, processes.get(processes.size() - 1).burst_time);
            double throughputSJF = calculateThroughput(processes, processes.get(processes.size() - 1).burst_time);
            writeAnalysisToFile("analysis.txt", "Shortest Job First (SJF) Scheduling", avgWaitingTimeSJF, avgTurnaroundTimeSJF, cpuUtilizationSJF, throughputSJF);

            priorityScheduling(processes);
            double avgWaitingTimePriority = calculateAverageWaitingTime(processes);
            double avgTurnaroundTimePriority = calculateAverageTurnaroundTime(processes);
            double cpuUtilizationPriority = calculateCPUUtilization(processes, processes.get(processes.size() - 1).burst_time);
            double throughputPriority = calculateThroughput(processes, processes.get(processes.size() - 1).burst_time);
            writeAnalysisToFile("analysis.txt", "Priority Scheduling", avgWaitingTimePriority, avgTurnaroundTimePriority, cpuUtilizationPriority, throughputPriority);

            processes.clear();
        }

        System.out.println("Analysis has been written to analysis.txt.");
    }
}