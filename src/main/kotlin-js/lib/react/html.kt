package lib.react

/**
 * Since Kotlin has no union types this cast says that [String] is [ReactElement]
 */
operator fun String?.unaryPlus() = unsafeCast<ReactElement?>()

/**
 * Helper function for html tags
 * TODO attributes
 */
fun tag(name: String,
        attrs: Attrs.() -> Unit = { },
        children: Array<out ReactElement?>)
        : ReactElement
        = React.createElement(name, Attrs().apply(attrs), *children)

open class Attrs {
    var className: String? = undefined
    var id: String? = undefined
    var style: Any? = undefined
    var tabIndex: Int? = undefined
    var title: String? = undefined
}

typealias HtmlBuilder = HtmlContent.() -> Unit

abstract class Html {
    protected abstract fun append(el: ReactElement?)
    protected abstract fun append(tag: Tag<*>)

    inner class Tag<A : Attrs>(val name: String, val attrF: () -> A) {
        init { append(this) }
        private var html = null as HtmlBuilder?
        private var attrs = null as A?
        operator fun invoke(f: HtmlBuilder = { }) { html = f }
        operator fun get(f: A.() -> Unit) = apply {
            attrs = attrs ?: attrF()
            attrs!!.f()
        }
        fun build() = React.createElement(name, attrs, *(html?.let(::htmls) ?: emptyArray()))
    }

    fun tag(name: String) = Tag(name, ::Attrs)

    val div get() = tag("div")
    val p get() = tag("p")
    val h1 get() = tag("h1")
    val h2 get() = tag("h2")
    val em get() = tag("em")
    val hr get() = tag("hr")
}

abstract class HtmlContent : Html() {

    inline operator fun <reified C, P> P.unaryPlus()
            where C : Any, P : Props<C>
            = + React.createElement(C::class.js, this)

    operator fun ReactElement?.unaryPlus() = append(this)
    operator fun String.unaryPlus() = + unsafeCast<ReactElement>()
    operator fun Array<out ReactElement?>.unaryPlus() = forEach { + it }
    operator fun Iterable<ReactElement?>.unaryPlus() = forEach { + it }
}

fun htmls(f: HtmlBuilder): Array<out ReactElement?> {
    var children = listOf<ReactElement?>()
    var tag = null as Html.Tag<*>?
    fun flushTag(newTag: Html.Tag<*>? = null) {
        tag?.let { children += it.build() }
        tag = newTag
    }
    object : HtmlContent() {
        override fun append(tag: Tag<*>) = flushTag(tag)
        override fun append(el: ReactElement?) {
            flushTag()
            children += el
        }
    }.f()
    flushTag()
    return children.toTypedArray()
}

fun html(f: Html.() -> Unit): ReactElement? {
    var element = null as ReactElement?
    var lastTag = null as Html.Tag<*>?
    object : Html() {
        override fun append(el: ReactElement?) {
            if (element != null || lastTag != null) throw IllegalStateException("Only one element expected")
            element = el
        }
        override fun append(tag: Tag<*>) {
            if (element != null || lastTag != null) throw IllegalStateException("Only one element expected")
            lastTag = tag
        }
    }.f()
    return element ?: lastTag?.build()
}