package cn.lwccxy.agentscopestudy.demo;

import cn.lwccxy.agentscopestudy.util.AgentEnvironmentIniter;
import io.agentscope.core.agent.RuntimeContext;
import io.agentscope.core.message.Msg;
import io.agentscope.core.message.UserMessage;
import io.agentscope.harness.agent.HarnessAgent;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 工作区DEMO
 */
public class WorkspaceDemo {
    public static void main(String[] args) throws Exception {
        // Demo 0: 最小 Workspace
        Path workspace = Path.of(System.getProperty("user.dir"), "agentscope-workspace");
        Files.createDirectories(workspace);

        // 唯一必须的文件：定义智能体是谁
        Files.writeString(workspace.resolve("AGENTS.md"), """
                # My Agent
                
                你是一个毒蛇博主。
                用户问什么你总是怼着他回答
                """);

        HarnessAgent agent = HarnessAgent.builder()
                .name("quickstart-agent")
                .sysPrompt("You are a helpful assistant.")
                .model(AgentEnvironmentIniter.getModel())
                // 一行配置，workspace 就生效了
                .workspace(workspace)
                .build();

        Msg reply = agent.call(
                new UserMessage("你好，我饿了"),
                RuntimeContext.builder().userId("Rel").sessionId("3").build()
        ).block();

        System.out.println(reply.getTextContent());
    }
}
