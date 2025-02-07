package cn.hippo4j.starter.monitor;

import cn.hippo4j.common.monitor.Message;

/**
 * Collect dynamic thread pool data.
 *
 * @author chen.ma
 * @date 2021/12/7 20:11
 */
public interface Collect {

    /**
     * Collect message.
     *
     * @return
     */
    Message collectMessage();

}
