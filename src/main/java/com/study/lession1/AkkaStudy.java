package com.study.lession1;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class AkkaStudy {
    public static void main(String[] args) {
        ActorRef<String> testSystem = (ActorRef<String>) ActorSystem.create(Main.create(), "testSystem");
        testSystem.tell("start");
    }
}

class PrintMyActorRefActor extends AbstractBehavior<String> {

    static Behavior<String> create() {
        return Behaviors.setup(PrintMyActorRefActor::new);
    }

    public PrintMyActorRefActor(ActorContext<String> context) {
        super(context);
    }

    public Receive<String> createReceive() {
        return newReceiveBuilder().onMessageEquals("printit", this::printIt).build();
    }

    private Behavior<String> printIt() {
        ActorRef<String> secondRef = getContext().spawn(Behaviors.empty(), "second-actor");
        System.out.println("Second: " + secondRef);
        return this;
    }
}

class Main extends AbstractBehavior<String> {
    public Main(ActorContext<String> context) {
        super(context);
    }

    static Behavior<String> create() {
        return Behaviors.setup(Main::new);
    }

    @Override
    public Receive<String> createReceive() {
        return newReceiveBuilder().onMessageEquals("start", this::start).build();
    }

    private Behavior<String> start() {
        ActorRef<String> firstRef = getContext().spawn(PrintMyActorRefActor.create(), "first-actor");
        System.out.println("First: " + firstRef);
        firstRef.tell("printit");
        return Behaviors.same();
    }
}


























