package orderservice.message

import order.events.OrderCreatedEvent
import order.events.OrderDomainEvent


class EventHandler<T>(val klass: Class<T>){
    companion object{
        inline fun <reified T> create() : EventHandler<T> {
            return EventHandler(T::class.java)
        }
    }
   fun <B> checkType(type: B){
       when{
           klass.isAssignableFrom(type!!::class.java) -> println("true")
           else -> println("false")
       }
    }
}
interface Sender<M>{
    fun send(message : M)
}

//saga
//저장 -> 시리얼라이즈


abstract class EventPublisher<E,I,O>(private val handleTypeKlazz: Class<E>, val messageBuilder: (I) -> O){
    val sender : Sender<O> = object : Sender<O>{
        override fun send(message: O) {
            println(message)
        }
    }

    fun <H : OrderDomainEvent> isHandle(event: H) : Boolean {
        return when{
            handleTypeKlazz.isAssignableFrom(event::class.java) -> true
            else -> false
        }
    }
    //이벤트타입, 데이터를 받는다.
    //저수준 레벨과 엮어야 한다.
    //기본동작이 필요하다.이벤트 받아서, 타입 확인하고, 메세지를 만들어서 전송한다.
    open fun process(message : I){
        val message = messageBuilder(message)
        send(message)
    }
    private fun send(message: O){
        sender.send(message)
    }
}

class OrderEventPublisher : EventPublisher<OrderCreatedEvent,String,String>(
        OrderCreatedEvent::class.java,
        { message -> message })

sealed class Option<out A> {
    abstract fun isEmpty() : Boolean
    fun getOrElse(default: @UnsafeVariance A) : A = when(this){
        is None -> default
        is Some -> value
    }
    fun getOrElse(default:()-> @UnsafeVariance A) : A = when(this){
        is None -> default()
        is Some -> value
    }

    internal object None: Option<Nothing>() {
        override fun isEmpty(): Boolean = true
        override fun toString(): String = "None"
        override fun equals(other: Any?): Boolean  = other === None
        override fun hashCode(): Int = 0
    }

    internal data class Some<out A>(internal val value: A): Option<A>(){
        override fun isEmpty(): Boolean = false
    }
    //일종의 팩토리 메서드 처럼 생성을 위한 목적의 코드
    companion object {
        operator fun <A> invoke(a: A? = null):Option<A>
                = when(a){
            null -> None
            else -> Some(a)
        }
    }
}
fun getDefault() :Int = throw RuntimeException()
fun max(list: List<Int>) : Option<Int> = Option(list.max())
fun main(args:Array<String>) : Unit {

    val max1 = max(listOf(7,3,1,2)).getOrElse(::getDefault)
    val myList = listOf(1,2,3,4,5,6,7)
    myList.map{ value -> value * 2}

    println(max1)

}
