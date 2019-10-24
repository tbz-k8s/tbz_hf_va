# Difficulties

## Docker build

Nur im docker image drinn. Sonst läuft alle ohne Probleme.

```
[main] INFO io.javalin.Javalin - Starting Javalin ...
[main] INFO io.javalin.Javalin - Listening on http://localhost:7000/
[main] INFO io.javalin.Javalin - Javalin started in 193ms \o/
[qtp715521683-36] WARN io.javalin.Javalin - Uncaught exception
java.nio.file.NoSuchFileException: src/main/resources/vue
	at java.base/sun.nio.fs.UnixException.translateToIOException(UnixException.java:92)
	at java.base/sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:111)
	at java.base/sun.nio.fs.UnixException.rethrowAsIOException(UnixException.java:116)
	at java.base/sun.nio.fs.UnixFileAttributeViews$Basic.readAttributes(UnixFileAttributeViews.java:55)
	at java.base/sun.nio.fs.UnixFileSystemProvider.readAttributes(UnixFileSystemProvider.java:145)
	at java.base/sun.nio.fs.LinuxFileSystemProvider.readAttributes(LinuxFileSystemProvider.java:99)
	at java.base/java.nio.file.Files.readAttributes(Files.java:1763)
	at java.base/java.nio.file.FileTreeWalker.getAttributes(FileTreeWalker.java:219)
	at java.base/java.nio.file.FileTreeWalker.visit(FileTreeWalker.java:276)
	at java.base/java.nio.file.FileTreeWalker.walk(FileTreeWalker.java:322)
	at java.base/java.nio.file.FileTreeIterator.<init>(FileTreeIterator.java:71)
	at java.base/java.nio.file.Files.walk(Files.java:3820)
	at io.javalin.plugin.rendering.vue.JavalinVue.walkPaths$javalin(JavalinVue.kt:33)
	at io.javalin.plugin.rendering.vue.VueComponent.handle(JavalinVue.kt:63)
	at io.javalin.core.security.SecurityUtil.noopAccessManager(SecurityUtil.kt:22)
	at io.javalin.http.JavalinServlet$addHandler$protectedHandler$1.handle(JavalinServlet.kt:116)
	at io.javalin.http.JavalinServlet$service$2$1.invoke(JavalinServlet.kt:45)
	at io.javalin.http.JavalinServlet$service$2$1.invoke(JavalinServlet.kt:24)
	at io.javalin.http.JavalinServlet$service$1.invoke(JavalinServlet.kt:123)
	at io.javalin.http.JavalinServlet$service$2.invoke(JavalinServlet.kt:40)
	at io.javalin.http.JavalinServlet.service(JavalinServlet.kt:75)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:790)
	at org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:852)
	at org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:544)
	at org.eclipse.jetty.server.handler.ScopedHandler.nextHandle(ScopedHandler.java:233)
	at org.eclipse.jetty.server.session.SessionHandler.doHandle(SessionHandler.java:1581)
	at org.eclipse.jetty.server.handler.ScopedHandler.nextHandle(ScopedHandler.java:233)
	at io.javalin.core.JavalinServer$start$httpHandler$1.doHandle(JavalinServer.kt:53)
	at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:188)
	at org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:482)
	at org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:1549)
	at org.eclipse.jetty.server.handler.ScopedHandler.nextScope(ScopedHandler.java:186)
	at org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1204)
	at org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:141)
	at org.eclipse.jetty.server.handler.HandlerList.handle(HandlerList.java:59)
	at org.eclipse.jetty.server.handler.StatisticsHandler.handle(StatisticsHandler.java:173)
	at org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:127)
	at org.eclipse.jetty.server.Server.handle(Server.java:494)
	at org.eclipse.jetty.server.HttpChannel.handle(HttpChannel.java:374)
	at org.eclipse.jetty.server.HttpConnection.onFillable(HttpConnection.java:268)
	at org.eclipse.jetty.io.AbstractConnection$ReadCallback.succeeded(AbstractConnection.java:311)
	at org.eclipse.jetty.io.FillInterest.fillable(FillInterest.java:103)
	at org.eclipse.jetty.io.ChannelEndPoint$2.run(ChannelEndPoint.java:117)
	at org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:782)
	at org.eclipse.jetty.util.thread.QueuedThreadPool$Runner.run(QueuedThreadPool.java:918)
	at java.base/java.lang.Thread.run(Thread.java:834)
```

## Libraries
