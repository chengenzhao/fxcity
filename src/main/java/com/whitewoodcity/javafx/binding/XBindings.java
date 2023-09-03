package com.whitewoodcity.javafx.binding;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.value.ObservableValue;

import static javafx.beans.binding.Bindings.createObjectBinding;

public class XBindings {

  @FunctionalInterface
  public interface Callable2<T0, T1, T> {
    T call(T0 t0, T1 t1);
  }

  public static <T0, T1, T> ObjectBinding<T> reduce(ObservableValue<T0> t0, ObservableValue<T1> t1, Callable2<T0, T1, T> callable) {
    return createObjectBinding(() -> callable.call(t0.getValue(), t1.getValue()), t0, t1);
  }

  @FunctionalInterface
  public interface Callable3<t0, T1, T2, T> {
    T call(t0 t0, T1 t1, T2 t2);
  }

  public static <T0, T1, T2, T> ObjectBinding<T> reduce(ObservableValue<T0> t0, ObservableValue<T1> t1, ObservableValue<T2> t2, Callable3<T0, T1, T2, T> callable) {
    return createObjectBinding(() -> callable.call(t0.getValue(), t1.getValue(), t2.getValue()), t0, t1, t2);
  }

  @FunctionalInterface
  public interface Callable4<t0, T1, T2, T3, T> {
    T call(t0 t0, T1 t1, T2 t2, T3 t3);
  }

  public static <T0, T1, T2, T3, T> ObjectBinding<T> reduce(ObservableValue<T0> t0, ObservableValue<T1> t1, ObservableValue<T2> t2, ObservableValue<T3> t3, Callable4<T0, T1, T2, T3, T> callable) {
    return createObjectBinding(() -> callable.call(t0.getValue(), t1.getValue(), t2.getValue(), t3.getValue()), t0, t1, t2, t3);
  }

  @FunctionalInterface
  public interface Callable5<t0, T1, T2, T3, T4, T> {
    T call(t0 t0, T1 t1, T2 t2, T3 t3, T4 t4);
  }

  public static <T0, T1, T2, T3, T4, T> ObjectBinding<T> reduce(ObservableValue<T0> t0, ObservableValue<T1> t1, ObservableValue<T2> t2, ObservableValue<T3> t3, ObservableValue<T4> t4, Callable5<T0, T1, T2, T3, T4, T> callable) {
    return createObjectBinding(() -> callable.call(t0.getValue(), t1.getValue(), t2.getValue(), t3.getValue(), t4.getValue()), t0, t1, t2, t3, t4);
  }

  @FunctionalInterface
  public interface Callable6<t0, T1, T2, T3, T4, T5, T> {
    T call(t0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
  }

  public static <T0, T1, T2, T3, T4, T5, T> ObjectBinding<T> reduce(ObservableValue<T0> t0, ObservableValue<T1> t1, ObservableValue<T2> t2, ObservableValue<T3> t3, ObservableValue<T4> t4, ObservableValue<T5> t5, Callable6<T0, T1, T2, T3, T4, T5, T> callable) {
    return createObjectBinding(() -> callable.call(t0.getValue(), t1.getValue(), t2.getValue(), t3.getValue(), t4.getValue(), t5.getValue()), t0, t1, t2, t3, t4, t5);
  }

  @FunctionalInterface
  public interface Callable7<t0, T1, T2, T3, T4, T5, T6, T> {
    T call(t0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);
  }

  public static <T0, T1, T2, T3, T4, T5, T6, T> ObjectBinding<T> reduce(ObservableValue<T0> t0, ObservableValue<T1> t1, ObservableValue<T2> t2, ObservableValue<T3> t3, ObservableValue<T4> t4, ObservableValue<T5> t5, ObservableValue<T6> t6, Callable7<T0, T1, T2, T3, T4, T5, T6, T> callable) {
    return createObjectBinding(() -> callable.call(t0.getValue(), t1.getValue(), t2.getValue(), t3.getValue(), t4.getValue(), t5.getValue(), t6.getValue()), t0, t1, t2, t3, t4, t5, t6);
  }

  @FunctionalInterface
  public interface Callable8<t0, T1, T2, T3, T4, T5, T6, T7, T> {
    T call(t0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);
  }

  public static <T0, T1, T2, T3, T4, T5, T6, T7, T> ObjectBinding<T> reduce(ObservableValue<T0> t0, ObservableValue<T1> t1, ObservableValue<T2> t2, ObservableValue<T3> t3, ObservableValue<T4> t4, ObservableValue<T5> t5, ObservableValue<T6> t6, ObservableValue<T7> t7, Callable8<T0, T1, T2, T3, T4, T5, T6, T7, T> callable) {
    return createObjectBinding(() -> callable.call(t0.getValue(), t1.getValue(), t2.getValue(), t3.getValue(), t4.getValue(), t5.getValue(), t6.getValue(), t7.getValue()), t0, t1, t2, t3, t4, t5, t6, t7);
  }

  @FunctionalInterface
  public interface Callable9<t0, T1, T2, T3, T4, T5, T6, T7, T8, T> {
    T call(t0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8);
  }

  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T> ObjectBinding<T> reduce(ObservableValue<T0> t0, ObservableValue<T1> t1, ObservableValue<T2> t2, ObservableValue<T3> t3, ObservableValue<T4> t4, ObservableValue<T5> t5, ObservableValue<T6> t6, ObservableValue<T7> t7, ObservableValue<T8> t8, Callable9<T0, T1, T2, T3, T4, T5, T6, T7, T8, T> callable) {
    return createObjectBinding(() -> callable.call(t0.getValue(), t1.getValue(), t2.getValue(), t3.getValue(), t4.getValue(), t5.getValue(), t6.getValue(), t7.getValue(), t8.getValue()), t0, t1, t2, t3, t4, t5, t6, t7, t8);
  }

  @FunctionalInterface
  public interface Callable10<t0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T> {
    T call(t0 t0, T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);
  }

  public static <T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T> ObjectBinding<T> reduce(ObservableValue<T0> t0, ObservableValue<T1> t1, ObservableValue<T2> t2, ObservableValue<T3> t3, ObservableValue<T4> t4, ObservableValue<T5> t5, ObservableValue<T6> t6, ObservableValue<T7> t7, ObservableValue<T8> t8, ObservableValue<T9> t9, Callable10<T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T> callable) {
    return createObjectBinding(() -> callable.call(t0.getValue(), t1.getValue(), t2.getValue(), t3.getValue(), t4.getValue(), t5.getValue(), t6.getValue(), t7.getValue(), t8.getValue(), t9.getValue()), t0, t1, t2, t3, t4, t5, t6, t7, t8, t9);
  }
}
