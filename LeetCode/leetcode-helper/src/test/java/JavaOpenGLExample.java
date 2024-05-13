import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

/**
 * @author Garry
 * ---------2024/5/13 10:23
 **/
public class JavaOpenGLExample {
    public static void main(String[] args) {
        // 初始化LWJGL
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // 创建窗口
        long window = GLFW.glfwCreateWindow(800, 600, "Java OpenGL Example", 0, 0);
        if (window == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // 设置视口
        GL.createCapabilities();
        GLFW.glfwMakeContextCurrent(window);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 800, 600, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);

        // 主循环
        while (!GLFW.glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT);

            // 绘制三角形
            glBegin(GL_TRIANGLES);
            glColor3f(1, 0, 0); // 红色
            glVertex2f(-0.5f, -0.5f);

            glColor3f(0, 1, 0); // 绿色
            glVertex2f(0.5f, -0.5f);

            glColor3f(0, 0, 1); // 蓝色
            glVertex2f(0.0f, 0.5f);
            glEnd();

            // 交换缓冲区
            GLFW.glfwSwapBuffers(window);

            // 处理事件
            GLFW.glfwPollEvents();
        }

        // 清理资源
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        MemoryUtil.memFree(caps);
    }
}
