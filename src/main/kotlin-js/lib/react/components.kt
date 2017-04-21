package lib.react

import kotlin.reflect.KClass

/**
 * Base type for component properties
 * WARNING: inherited props could have strange js names
 */
interface Props<in C : React.Component<*>>

/**
 * Converter function that allows to use property object as element definition
 * i.e.`+ Node(param = 1)`
 */
inline operator fun <reified C, P> P.unaryPlus()
        where C : Any, P : Props<C>
        = React.createElement(C::class.js, this)

/**
 * Converter for components without properties
 * usage: `+ Comp::class`
 */
operator fun <C : React.Component<Nothing>> KClass<C>.unaryPlus()
        = React.createElement(js)

/**
 * Base type for functional components
 */
abstract class SimpleComponent<P>(val content: P.() -> ReactElement?)
    : React.PureComponent<P>() {
    override fun render() = props.content()
}