/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/08/21 15:21:07
 *
 * kar-framework/kar-framework.common.main/ConcurrentNodeDeque.java
 */

package io.github.karlatemp.karframework.utils;

import io.github.karlatemp.karframework.annotation.Comment;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConcurrentNodeDeque<Type> implements Iterable<ConcurrentNodeDeque.Node<Type>> {
    public static final class UnsafeAccess {
        private static final UnsafeAccess INSTANCE = new UnsafeAccess();

        private UnsafeAccess() {
        }

        @Deprecated
        @Comment("Unsafe access.")
        public static UnsafeAccess getInstance() {
            return INSTANCE;
        }

        public <T> void setPrevious(Node<T> thiz, Node<T> previous) {
            thiz.previous = previous;
        }

        public <T> void setNext(Node<T> thiz, Node<T> next) {
            thiz.next = next;
        }

        public boolean tryLock(Node<?> node) {
            return node.tryLock();
        }

        public void forceLock(Node<?> node) {
            node.lock.set(true);
        }

        public void releaseLock(Node<?> node) {
            node.release();
        }

        public Node.NodeStatus getStatusField(Node<?> node) {
            return node.status;
        }

        public void setStatusField(Node<?> node, Node.NodeStatus status) {
            node.status = status;
        }

    }

    public static class Node<NodeType> {
        public enum NodeStatus {
            ALLOCATED, HEAD, TAIL, INSERTED, REMOVED
        }

        protected final AtomicBoolean lock = new AtomicBoolean();
        protected Node<NodeType> previous, next;
        protected NodeStatus status = NodeStatus.ALLOCATED;
        protected NodeType value;

        public Node<NodeType> getPrevious() {
            return previous;
        }

        public Node<NodeType> getNext() {
            return next;
        }

        public NodeStatus getStatus() {
            return status;
        }

        public Node() {
        }

        public Node(NodeType value) {
            this.value = value;
        }

        public NodeType getValue() {
            return value;
        }

        public void setValue(NodeType value) {
            this.value = value;
        }

        protected boolean tryLock() {
            return lock.compareAndSet(false, true);
        }

        protected void release() {
            lock.set(false);
        }

        public void insertToAfter(Node<NodeType> node) {
            while (true) {
                if (node.tryLock()) {
                    {
                        NodeStatus nodeStatus = node.status;
                        if (!(nodeStatus == NodeStatus.ALLOCATED || nodeStatus == NodeStatus.REMOVED)) {
                            node.release();
                            throw new IllegalStateException(
                                    "Cannot insert node with status: " + nodeStatus
                            );
                        }
                    }
                    if (!this.tryLock()) {
                        node.release();
                        continue;
                    }
                    Node<NodeType> next = this.next;
                    if (next == null) {
                        // No next;
                        this.next = node;
                        node.previous = this;
                        this.status = NodeStatus.INSERTED;
                        this.status = getStatus();
                        node.status = NodeStatus.INSERTED;
                        this.release();
                        node.release();
                        return;
                    }
                    if (!next.tryLock()) {
                        node.release();
                        this.release();
                        continue;
                    }
                    this.next = node;
                    node.next = next;
                    node.previous = this;
                    next.previous = node;
                    this.release();
                    node.status = NodeStatus.INSERTED;
                    node.release();
                    next.release();
                    return;
                }
            }
        }

        public void insertToBefore(Node<NodeType> node) {
            while (true) {
                if (node.tryLock()) {
                    {
                        NodeStatus nodeStatus = node.status;
                        if (!(nodeStatus == NodeStatus.ALLOCATED || nodeStatus == NodeStatus.REMOVED)) {
                            node.release();
                            throw new IllegalStateException(
                                    "Cannot insert node with status: " + nodeStatus
                            );
                        }
                    }
                    if (!this.tryLock()) {
                        node.release();
                        continue;
                    }
                    Node<NodeType> previous = this.previous;
                    if (previous == null) {
                        this.previous = node;
                        node.next = this;
                        this.status = NodeStatus.INSERTED;
                        this.status = getStatus();
                        node.status = NodeStatus.INSERTED;
                        this.release();
                        node.release();
                        return;
                    }
                    if (!previous.tryLock()) {
                        this.release();
                        node.release();
                        continue;
                    }
                    this.previous = node;
                    node.previous = previous;
                    previous.next = node;
                    node.next = this;
                    node.status = NodeStatus.INSERTED;
                    this.release();
                    previous.release();
                    node.release();
                    return;
                }
            }
        }

        public void remove() {
            while (true) {
                if (this.tryLock()) {
                    Node<NodeType> previous = this.previous,
                            next = this.next;
                    if (previous != null) {
                        if (!previous.tryLock()) {
                            this.release();
                            continue;
                        }
                    }
                    if (next != null) {
                        if (!next.tryLock()) {
                            this.release();
                            if (previous != null) previous.release();
                            continue;
                        }
                    }
                    this.status = NodeStatus.REMOVED;
                    if (previous != null) previous.next = next;
                    if (next != null) next.previous = previous;
                    if (previous != null) previous.release();
                    if (next != null) next.release();
                    this.release();
                    return;
                }
            }
        }

        public void unlink() {
            remove();
            while (true) {
                if (tryLock()) {
                    this.previous = null;
                    this.next = null;
                    release();
                    return;
                }
            }
        }

        @Override
        public String toString() {
            return "Node{" +
                    "status=" + status +
                    ", value=" + value +
                    '}';
        }
    }

    public final Node<Type> head = new Node<Type>() {
        @Override
        public NodeStatus getStatus() {
            return NodeStatus.HEAD;
        }

        @Override
        public void insertToBefore(Node<Type> node) {
            insertToAfter(node);
        }

        @Override
        public Type getValue() {
            return null;
        }

        @Override
        public void setValue(Type value) {
        }

        @Override
        public void remove() {
        }

        @Override
        public void unlink() {
            Node<Type> t = tail;
            while (true) {
                if (tryLock()) {
                    if (!t.tryLock()) {
                        this.release();
                        continue;
                    }
                    this.next = t;
                    t.previous = this;
                    t.release();
                    this.release();
                    return;
                }
            }
        }

        @Override
        public String toString() {
            return "Head";
        }
    }, tail = new Node<Type>() {
        @Override
        public void insertToAfter(Node<Type> node) {
            insertToBefore(node);
        }

        @Override
        public Type getValue() {
            return null;
        }

        @Override
        public NodeStatus getStatus() {
            return NodeStatus.TAIL;
        }

        @Override
        public void setValue(Type value) {
        }

        @Override
        public void remove() {
        }

        @Override
        public void unlink() {
            head.unlink();
        }

        {
            head.next = this;
            this.previous = head;
        }

        @Override
        public String toString() {
            return "Tail";
        }
    };

    public boolean isEmpty() {
        return head.next == tail;
    }

    public static class NodeIterator<NodeType>
            implements Iterator<Node<NodeType>> {
        protected Node<NodeType> current;

        protected Node<NodeType> nextNode() {
            return current.next;
        }

        public NodeIterator(Node<NodeType> start) {
            this.current = start;
        }

        @Override
        public boolean hasNext() {
            loop:
            while (current != null) {
                switch (current.getStatus()) {
                    case HEAD:
                    case TAIL:
                    case REMOVED:
                        current = nextNode();
                        break;
                    default:
                        break loop;
                }
            }
            return current != null;
        }

        @Override
        public Node<NodeType> next() {
            Node<NodeType> c = current;
            current = nextNode();
            return c;
        }
    }

    public static class ReversedNodeIterator<NodeType>
            extends NodeIterator<NodeType> {
        public ReversedNodeIterator(Node<NodeType> start) {
            super(start);
        }

        @Override
        protected Node<NodeType> nextNode() {
            return current.previous;
        }
    }

    @NotNull
    @Override
    public Iterator<Node<Type>> iterator() {
        return new NodeIterator<>(head);
    }

    @NotNull
    public Iterator<Node<Type>> reversedIterator() {
        return new ReversedNodeIterator<>(tail);
    }

    public Node<Type> addLast(Type value) {
        Node<Type> node = new Node<>(value);
        tail.insertToBefore(node);
        return node;
    }

    public Node<Type> addFirst(Type value) {
        Node<Type> node = new Node<>(value);
        head.insertToAfter(node);
        return node;
    }
}
