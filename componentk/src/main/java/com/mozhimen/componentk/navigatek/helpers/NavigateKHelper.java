package com.mozhimen.componentk.navigatek.helpers;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.NavigatorProvider;

import com.mozhimen.basick.utilk.content.UtilKContext;
import com.mozhimen.basick.utilk.res.UtilKRes;
import com.mozhimen.componentk.R;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName NavigateKHelper
 * @Description 浅改造
 * @Author Kolin Zhao
 * @Date 2021/9/6 17:28
 * @Version 1.0
 */

/**
 * Navigator that navigates through {@link FragmentTransaction fragment transactions}. Every
 * destination using this Navigator must set a valid Fragment class name with
 * <code>android:name</code> or {@link Destination#setClassName(String)}.
 * <p>
 * The current Fragment from FragmentNavigator's perspective can be retrieved by calling
 * {@link FragmentManager#getPrimaryNavigationFragment()} with the FragmentManager
 * passed to this FragmentNavigator.
 * <p>
 * Note that the default implementation does Fragment transactions
 * asynchronously, so the current Fragment will not be available immediately
 * (i.e., in callbacks to {@link NavController.OnDestinationChangedListener}).
 */
@Navigator.Name("navigatek")
public class NavigateKHelper extends Navigator<NavigateKHelper.Destination> {
    private static final String KEY_BACK_STACK_IDS = "androidx-nav-fragment:navigator:backStackIds";

    private final Context _context;
    private final FragmentManager _fragmentManager;
    private final int _containerId;
    private ArrayDeque<Integer> _backStack = new ArrayDeque<>();
    private final String TAG = "NavigateKHelper>>>>>";

    public NavigateKHelper(@NonNull Context context, @NonNull FragmentManager manager,
                           int containerId) {
        _context = context;
        _fragmentManager = manager;
        _containerId = containerId;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method must call
     * {@link FragmentTransaction#setPrimaryNavigationFragment(Fragment)}
     * if the pop succeeded so that the newly visible Fragment can be retrieved with
     * {@link FragmentManager#getPrimaryNavigationFragment()}.
     * <p>
     * Note that the default implementation pops the Fragment
     * asynchronously, so the newly visible Fragment from the back stack
     * is not instantly available after this call completes.
     */
    @Override
    public boolean popBackStack() {
        if (_backStack.isEmpty()) {
            return false;
        }
        if (_fragmentManager.isStateSaved()) {
            Log.d(TAG, "Ignoring popBackStack() call: FragmentManager has already saved its state");
            return false;
        }
        _fragmentManager.popBackStack(
                generateBackStackName(_backStack.size(), _backStack.peekLast()),
                FragmentManager.POP_BACK_STACK_INCLUSIVE);
        _backStack.removeLast();
        return true;
    }

    @NonNull
    @Override
    public Destination createDestination() {
        return new Destination(this);
    }

    /**
     * Instantiates the Fragment via the FragmentManager's
     * {@link FragmentFactory}.
     * <p>
     * Note that this method is <strong>not</strong> responsible for calling
     * {@link Fragment#setArguments(Bundle)} on the returned Fragment instance.
     *
     * @param context         Context providing the correct {@link ClassLoader}
     * @param fragmentManager FragmentManager the Fragment will be added to
     * @param className       The Fragment to instantiate
     * @param args            The Fragment's arguments, if any
     * @return A new fragment instance.
     * @deprecated Set a custom {@link FragmentFactory} via
     * {@link FragmentManager#setFragmentFactory(FragmentFactory)} to control
     * instantiation of Fragments.
     */
    @SuppressWarnings("DeprecatedIsStillUsed") // needed to maintain forward compatibility
    @Deprecated
    @NonNull
    public Fragment instantiateFragment(@NonNull Context context,
                                        @NonNull FragmentManager fragmentManager,
                                        @NonNull String className, @SuppressWarnings("unused") @Nullable Bundle args) {
        return fragmentManager.getFragmentFactory().instantiate(
                UtilKContext.getClassLoader(context), className);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method should always call
     * {@link FragmentTransaction#setPrimaryNavigationFragment(Fragment)}
     * so that the Fragment associated with the new destination can be retrieved with
     * {@link FragmentManager#getPrimaryNavigationFragment()}.
     * <p>
     * Note that the default implementation commits the new Fragment
     * asynchronously, so the new Fragment is not instantly available
     * after this call completes.
     */
    @SuppressWarnings("deprecation") /* Using instantiateFragment for forward compatibility */
    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args,
                                   @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras) {
        if (_fragmentManager.isStateSaved()) {
            Log.d(TAG, "Ignoring navigate() call: FragmentManager has already" + " saved its state");
            return null;
        }
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = UtilKContext.getPackageName(_context) + className;
        }

        //android.fragment.app.xxx->xxx
        String tag = className.substring(className.lastIndexOf(".") + 1);
        Fragment frag = _fragmentManager.findFragmentByTag(tag);
        if (frag == null) {
            frag = instantiateFragment(_context, _fragmentManager,
                    className, args);
        }

        frag.setArguments(args);
        final FragmentTransaction ft = _fragmentManager.beginTransaction();

        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = enterAnim != -1 ? enterAnim : 0;
            exitAnim = exitAnim != -1 ? exitAnim : 0;
            popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
            popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }

        //ft.replace(mContainerId, frag);
        List<Fragment> fragments = _fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            ft.hide(fragment);
        }
        if (!frag.isAdded()) {
            ft.add(_containerId, frag, tag);
        }
        ft.show(frag);
        ft.setPrimaryNavigationFragment(frag);

        final @IdRes int destId = destination.getId();
        final boolean initialNavigation = _backStack.isEmpty();
        final boolean isSingleTaskReplacement = !initialNavigation && _backStack.contains(destId);
        final boolean isSingleTopReplacement = navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && _backStack.peekLast() == destId;// Build first class singleTop behavior for fragments

        boolean isAdded;
        if (initialNavigation) {
            isAdded = true;
            Log.d(TAG, "navigate: initialNavigation");
        } else if (isSingleTaskReplacement) {
            isAdded = false;
            Log.d(TAG, "navigate: isSingleTaskReplacement");
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (_backStack.size() > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                _fragmentManager.popBackStack(
                        generateBackStackName(_backStack.size(), _backStack.peekLast()),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.addToBackStack(generateBackStackName(_backStack.size(), destId));
            }
            isAdded = false;
            Log.d(TAG, "navigate: isSingleTopReplacement");
        } else {
            ft.addToBackStack(generateBackStackName(_backStack.size() + 1, destId));
            isAdded = true;
            Log.d(TAG, "navigate: else");
        }
        if (navigatorExtras instanceof Extras) {
            Extras extras = (Extras) navigatorExtras;
            for (Map.Entry<View, String> sharedElement : extras.getSharedElements().entrySet()) {
                ft.addSharedElement(sharedElement.getKey(), sharedElement.getValue());
            }
        }
        ft.setReorderingAllowed(true);
        ft.commit();
        // The commit succeeded, update our view of the world
        if (isAdded) {
            _backStack.add(destId);
            Log.d(TAG, "navigate: " + _backStack.size());
            return destination;
        } else if (isSingleTaskReplacement) {
            return destination;
        } else {
            return null;
        }
    }

    @Override
    @Nullable
    public Bundle onSaveState() {
        Bundle b = new Bundle();
        int[] backStack = new int[_backStack.size()];
        int index = 0;
        for (Integer id : _backStack) {
            backStack[index++] = id;
        }
        b.putIntArray(KEY_BACK_STACK_IDS, backStack);
        return b;
    }

    @Override
    public void onRestoreState(@Nullable Bundle savedState) {
        if (savedState != null) {
            int[] backStack = savedState.getIntArray(KEY_BACK_STACK_IDS);
            if (backStack != null) {
                _backStack.clear();
                for (int destId : backStack) {
                    _backStack.add(destId);
                }
            }
        }
    }

    @NonNull
    private String generateBackStackName(int backStackIndex, int destId) {
        return backStackIndex + "-" + destId;
    }

    /**
     * NavDestination specific to {@link androidx.navigation.fragment.FragmentNavigator}
     */
    @NavDestination.ClassType(Fragment.class)
    public static class Destination extends NavDestination {

        private String mClassName;

        /**
         * Construct a new fragment destination. This destination is not valid until you set the
         * Fragment via {@link #setClassName(String)}.
         *
         * @param navigatorProvider The {@link NavController} which this destination
         *                          will be associated with.
         */
        public Destination(@NonNull NavigatorProvider navigatorProvider) {
            this(navigatorProvider.getNavigator(NavigateKHelper.class));
        }

        /**
         * Construct a new fragment destination. This destination is not valid until you set the
         * Fragment via {@link #setClassName(String)}.
         *
         * @param fragmentNavigator The {@link androidx.navigation.fragment.FragmentNavigator} which this destination
         *                          will be associated with. Generally retrieved via a
         *                          {@link NavController}'s
         *                          {@link NavigatorProvider#getNavigator(Class)} method.
         */
        public Destination(@NonNull Navigator<? extends Destination> fragmentNavigator) {
            super(fragmentNavigator);
        }

        @CallSuper
        @Override
        public void onInflate(@NonNull Context context, @NonNull AttributeSet attrs) {
            super.onInflate(context, attrs);
            TypedArray a = UtilKRes.getAppResources().obtainAttributes(attrs,
                    androidx.navigation.fragment.R.styleable.FragmentNavigator);
            String className = a.getString(androidx.navigation.fragment.R.styleable.FragmentNavigator_android_name);
            if (className != null) {
                setClassName(className);
            }
            a.recycle();
        }

        /**
         * Set the Fragment class name associated with this destination
         *
         * @param className The class name of the Fragment to show when you navigate to this
         *                  destination
         * @return this {@link Destination}
         */
        @NonNull
        public final Destination setClassName(@NonNull String className) {
            mClassName = className;
            return this;
        }

        /**
         * Gets the Fragment's class name associated with this destination
         *
         * @throws IllegalStateException when no Fragment class was set.
         */
        @NonNull
        public final String getClassName() {
            if (mClassName == null) {
                throw new IllegalStateException("Fragment class was not set");
            }
            return mClassName;
        }

        @NonNull
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(super.toString());
            sb.append(" class=");
            if (mClassName == null) {
                sb.append("null");
            } else {
                sb.append(mClassName);
            }
            return sb.toString();
        }
    }

    /**
     * Extras that can be passed to FragmentNavigator to enable Fragment specific behavior
     */
    public static final class Extras implements Navigator.Extras {
        private final LinkedHashMap<View, String> mSharedElements = new LinkedHashMap<>();

        Extras(Map<View, String> sharedElements) {
            mSharedElements.putAll(sharedElements);
        }

        /**
         * Gets the map of shared elements associated with these Extras. The returned map
         * is an {@link Collections#unmodifiableMap(Map) unmodifiable} copy of the underlying
         * map and should be treated as immutable.
         */
        @NonNull
        public Map<View, String> getSharedElements() {
            return Collections.unmodifiableMap(mSharedElements);
        }

        /**
         * Builder for constructing new {@link Extras} instances. The resulting instances are
         * immutable.
         */
        public static final class Builder {
            private final LinkedHashMap<View, String> mSharedElements = new LinkedHashMap<>();

            /**
             * Adds multiple shared elements for mapping Views in the current Fragment to
             * transitionNames in the Fragment being navigated to.
             *
             * @param sharedElements Shared element pairs to add
             * @return this {@link Builder}
             */
            @NonNull
            public Builder addSharedElements(@NonNull Map<View, String> sharedElements) {
                for (Map.Entry<View, String> sharedElement : sharedElements.entrySet()) {
                    View view = sharedElement.getKey();
                    String name = sharedElement.getValue();
                    if (view != null && name != null) {
                        addSharedElement(view, name);
                    }
                }
                return this;
            }

            /**
             * Maps the given View in the current Fragment to the given transition name in the
             * Fragment being navigated to.
             *
             * @param sharedElement A View in the current Fragment to match with a View in the
             *                      Fragment being navigated to.
             * @param name          The transitionName of the View in the Fragment being navigated to that
             *                      should be matched to the shared element.
             * @return this {@link Builder}
             * @see FragmentTransaction#addSharedElement(View, String)
             */
            @NonNull
            public Builder addSharedElement(@NonNull View sharedElement, @NonNull String name) {
                mSharedElements.put(sharedElement, name);
                return this;
            }

            /**
             * Constructs the final {@link Extras} instance.
             *
             * @return An immutable {@link Extras} instance.
             */
            @NonNull
            public Extras build() {
                return new Extras(mSharedElements);
            }
        }
    }
}
