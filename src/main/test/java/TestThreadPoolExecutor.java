import java.util.ArrayList;
import java.util.concurrent.*;

public class TestThreadPoolExecutor {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,1,0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.AbortPolicy());
//        ExecutorCompletionService<Integer> executorCompletionService = new ExecutorCompletionService<>(executor);
        ArrayList<Thread> arrayList = new ArrayList<>();

        for (int i=0;i<20;i++){
            int tmp = i;
            Thread thread = new Thread(() -> {
                try {
                    Future<Integer> submit = executor.submit(() -> {
                        Thread.sleep(2000);
                        return tmp;
                    });
                    System.out.println(tmp+" submited");
                    System.out.println(tmp+" : "+submit.get());
                    System.out.println(tmp+" finished");
                }catch (RejectedExecutionException rejectedExecutionException){
                    System.out.println(tmp+" : -1");
                }catch (Exception e){
                    e.printStackTrace();
                }

            });
            arrayList.add(thread);
            thread.start();
        }


        for (Thread t:arrayList){
            t.join();
        }
        System.out.println("shutdown");
        executor.shutdown();
        while (!executor.isTerminated()){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



    }

}
