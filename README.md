# EventBus

A simple library for dispatching events and subscribing to them

## Example of usage
Subscription and event dispatch
```java
EventBus eventBus = new EventBusImpl();

eventBus.subscribe(ChatMessageEvent.class, event -> {
    System.out.println("Received ChatMessageEvent: " + event.getMessage());
    if (shouldCancel) {
        event.cancel();
    }
});

eventBus.subscribe(new AsyncChatListener());

eventBus.call(new ChatMessageEvent("Chat message"));
```
Subscribing to events using the `@Subscribe` annotation
```java
public class AsyncChatListener {
    @Subscribe(priority = EventPriorities.FIRST, handleCancelled = true, async = true)
    public void onChatMessage(ChatMessageEvent event) {
        System.out.println("Async chat event: " + event);
    }
}
```
Unsubscribe from events
```java
// Subscribing to events
Subscriber<ChatMessageEvent> subscriber = eventBus.subscribe(ChatMessageEvent.class, event -> {
    System.out.println("Received ChatMessageEvent: " + event.getMessage());
});

AsyncChatListener asyncChatListener = new AsyncChatListener();
eventBus.subscribe(asyncChatListener);

eventBus.call(new ChatMessageEvent("Chat message"));

// Unsubscribe from events
eventBus.unsubscribe(asyncChatListener);
eventBus.unsubscribe(subscriber);
```
Working with event result
```java
CallResult result = eventBus.callSilently(new ChatMessageEvent("Chat message"));
System.out.println("Exceptions: " + result.getExceptions());
System.out.println("Is success: " + result.isSuccess());
System.out.println("Is cancelled: " + result.isCancelled());
```
Creating an own event
```java
public class ChatMessageEvent implements Event, Cancellable {
    private final String message;
    private boolean cancelled;

    public ChatMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
```
___

## Maven
Adding repository:
```xml
<repositories>
    <repository>
        <id>densy-repository-snapshots</id>
        <url>https://repo.densy.org/snapshots</url>
    </repository>
</repositories>
```

Adding a library api:
```xml
<dependency>
    <groupId>org.densy.eventbus</groupId>
    <artifactId>api</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

Adding a library implementation:
```xml
<dependency>
    <groupId>org.densy.eventbus</groupId>
    <artifactId>core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Gradle
Adding repository:
```groovy
maven {
    name = "densyRepositorySnapshots"
    url = uri("https://repo.densy.org/snapshots")
}
```

Adding a library api:
```groovy
implementation "org.densy.eventbus:api:1.0.0-SNAPSHOT"
```

Adding a library implementation:
```groovy
implementation "org.densy.eventbus:core:1.0.0-SNAPSHOT"
```

___

### Special Thanks
Inspired by the [AllayMC event system](https://github.com/AllayMC/Allay/tree/master/api/src/main/java/org/allaymc/api/eventbus), I took some code from here.