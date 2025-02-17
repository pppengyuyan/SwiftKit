package com.mozhimen.basick.elemk.commons


/**
 * @ClassName IListener
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/3/13 14:50
 * @Version 1.0
 */
typealias IListener = () -> Unit;
typealias IValueListener<T> = (value: T) -> Unit;
typealias IValue2Listener<T> = (value1: T, value2: T) -> Unit;
typealias IValue2Listener2<T1, T2> = (value1: T1, value2: T2) -> Unit;
