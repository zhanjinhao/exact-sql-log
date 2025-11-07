package cn.addenda.exactsqllog.proxy.test.jdk;

import org.junit.jupiter.api.Test;

class InheritedTest {

  // todo 用JavaParser分析这种结构，会得到什么样的依赖数据
  @Test
  void test() {
    // a1
    // a2
    // -------
    // a1
    // b1
    // a2
    // b2
    A a = new A();
    a.echo2();
    System.out.println("-------");
    B b = new B();
    b.echo2();
  }

  static class A {
    public void echo1() {
      System.out.println("a1");
    }

    public void echo2() {
      this.echo1();
      System.out.println("a2");
    }
  }

  static class B extends A {
    @Override
    public void echo1() {
      super.echo1();
      System.out.println("b1");
    }

    @Override
    public void echo2() {
      super.echo2();
      System.out.println("b2");
    }
  }

}
