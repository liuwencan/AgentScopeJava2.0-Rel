package cn.lwccxy.agentscopestudy.demo;

import cn.lwccxy.agentscopestudy.util.AgentEnvironmentIniter;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.event.AgentEvent;
import io.agentscope.core.event.TextBlockDeltaEvent;
import io.agentscope.core.message.Msg;
import io.agentscope.harness.agent.HarnessAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.scheduler.Schedulers;

/**
 * 普通交互对话过程
 */
public class ChatDemo {

    private final static Logger logger = LoggerFactory.getLogger(ChatDemo.class);

    public static void main(String[] args) {
        HarnessAgent agent = HarnessAgent.builder()
                .name("RelAgent")
                .description("情感专家")
                .model(AgentEnvironmentIniter.getModel())
                .sysPrompt(AgentEnvironmentIniter.getSysPrompt())
                .build();


        String userMsg = "在恋爱之中, 你可以给到我一些什么建议？";

        // ======= 同步调用过程API ============
//        Msg msg = agent.call(userMsg, AgentEnvironmentIniter.getRuntimeContext())
//                .block();
//
//        logger.info(msg.getTextContent());

        // ======= 流式调用过程API =========
        agent.streamEvents(userMsg, AgentEnvironmentIniter.getRuntimeContext())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(event ->{
                    if (event instanceof TextBlockDeltaEvent){
                        logger.info(((TextBlockDeltaEvent) event).getDelta());
                    }
                }).blockLast();
    }

}
