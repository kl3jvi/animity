package com.kl3jvi.animity.ui.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/* It allows us to use the `T::class.java` syntax. */
inline fun <reified T : ViewBinding> Fragment.viewBinding() =
    FragmentViewBindingDelegate(T::class.java, this)

@Suppress("UNCHECKED_CAST")
class FragmentViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>,
    private val fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    /* A workaround for a bug in the AndroidX Data Binding library. */
    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            binding = null
                        }
                    })
                }
            }
        })
    }

    /**
     * If the binding is null, then we invoke the static method `bind` on the binding class, passing in
     * the view of the fragment
     *
     * @param thisRef Fragment - The fragment that the binding is being created for
     * @param property KProperty<*> - This is the property that is being delegated.
     * @return The binding object
     */
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        if (binding == null) {
            binding = bindingClass.getMethod("bind", View::class.java)
                .invoke(null, thisRef.requireView()) as T
        }
        return binding!!
    }
}
