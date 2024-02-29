import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.extra

inline var ExtensionAware.baseVersionCode
    get() = extra["baseVersionCode"] as Int
    set(value) = extra.set("baseVersionCode", value)
