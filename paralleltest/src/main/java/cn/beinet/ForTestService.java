package cn.beinet;

import cn.beinet.dto.TestInputDto;
import cn.beinet.dto.TestOutputDto;
import cn.beinet.utils.HttpHelper;
import lombok.Synchronized;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForTestService implements Runnable {
    private TestInputDto inputDto;
    private int threadNum;
    TestOutputDto ret = new TestOutputDto();

    public ForTestService(TestInputDto inputDto) {
        this.inputDto = inputDto;
    }

    public TestOutputDto startTest() throws InterruptedException {
        // 抛弃第一次请求，避免DNS解析等损耗
        sendRequest();

        ExecutorService pool = Executors.newCachedThreadPool();

        // 并行启动ConsurrencyNum个线程
        for (int i = 0; i < inputDto.getConcurrencyNum(); i++) {
            pool.execute(this);
        }

        // 等待任务完成
        do {
            Thread.sleep(10000);
            System.out.println(threadNum);
        } while (threadNum > 0);

        pool.shutdown();
        return ret;
    }

    @Override
    public void run() {
        long perTime = inputDto.getTimePerThread();
        addThread();
        try {
            // 每个线程顺序执行请求
            for (int requestIdx = 0; requestIdx < perTime; requestIdx++) {
                long costTime = sendRequest();
                ret.putTime(costTime);
            }
        } catch (Exception exp) {
            System.out.println("有线程出错了：" + exp.getMessage());
        } finally {
            decThread();
        }

    }

    @Synchronized
    private void addThread() {
        threadNum++;
    }

    @Synchronized
    private void decThread() {
        threadNum--;
    }

    /**
     * 发请求
     * @return 返回请求耗时，纳秒
     */
    private long sendRequest() {
        long beginTime = System.nanoTime();
        HttpHelper.GetPage(inputDto.getUrl(), "");
        return System.nanoTime() - beginTime;
    }
}
