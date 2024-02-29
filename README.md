This is a Minecraft plugin development assistant plugin.

### Usage
##### Script.java
```
import java.io.IOException;
import java.net.Socket;

public class Script {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", Integer.parseInt(args[0]))) {
            socket.getOutputStream().write(("develop " + args[1] + " " + args[2]).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
##### pom.xml
```
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>exec-maven-plugin</artifactId>
            <version>3.2.0</version>
            <executions>
                <execution>
                    <id>unload</id>
                    <phase>prepare-package</phase>
                    <goals>
                        <goal>java</goal>
                    </goals>
                    <configuration>
                        <mainClass>Script</mainClass>
                        <arguments>
                            <argument>8888</argument>
                            <argument>unload</argument>
                            <argument>PluginName</argument>
                        </arguments>
                    </configuration>
                </execution>
                <execution>
                    <id>load</id>
                    <phase>package</phase>
                    <goals>
                        <goal>java</goal>
                    </goals>
                    <configuration>
                        <mainClass>Script</mainClass>
                        <arguments>
                            <argument>8888</argument>
                            <argument>load</argument>
                            <argument>iloSp</argument>
                        </arguments>
                    </configuration>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <version>3.3.0</version>
            <configuration>
                <!-- jar包输出路径 -->
                <outputDirectory>F:\Game\Minecraft\plugins</outputDirectory>
                <excludes>
                    <exclude>**/*.properties</exclude>
                    <exclude>**/*.xml</exclude>
                    <exclude>static/**</exclude>
                    <exclude>templates/**</exclude>
                    <exclude>**/Script.class</exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```