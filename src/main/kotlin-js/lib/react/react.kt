package lib.react

/**
 * Pseudo interface for types, that can be react element children:
 *  - String
 *  - Function
 *  - React.Component ancestor
 *
 * common pattern -- use [unaryPlus] function to convert something to [ReactElement]
 */
interface ReactElement

/**
 * ReactJS binding
 */
external class React {
    companion object {
        fun createElement(component: Any,
                          props: Any? = definedExternally,
                          vararg children: ReactElement?)
                : ReactElement
    }

    abstract class Component<out P> {
        val props: P
        abstract fun render(): ReactElement?
    }

    abstract class PureComponent<out P> : Component<P>
}
