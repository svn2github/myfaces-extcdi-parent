/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.extensions.cdi.core.impl.utils;

import org.apache.myfaces.extensions.cdi.core.api.Advanced;
import org.apache.myfaces.extensions.cdi.core.api.projectstage.ProjectStage;
import org.apache.myfaces.extensions.cdi.core.api.provider.BeanManagerProvider;
import org.apache.myfaces.extensions.cdi.core.api.util.ClassUtils;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.Nonbinding;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * This is a collection of a few useful static helper functions.
 * <p/>
 */
public class CodiUtils
{
    //TODO change source
    public static final String CODI_PROPERTIES = "/META-INF/extcdi/extcdi.properties";

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static <T> T createNewInstanceOfBean(CreationalContext<T> creationalContext, Bean<T> bean)
    {
        return createNewInstanceOfBean(bean, creationalContext);
    }

    public static <T> T createNewInstanceOfBean(Bean<T> bean, CreationalContext<T> creationalContext)
    {
        return bean.create(creationalContext);
    }

    public static <T> T getOrCreateScopedInstanceOfBeanByName(
            BeanManager beanManager, String beanName, Class<T> targetClass)
    {
        Set<Bean<?>> foundBeans = beanManager.getBeans(beanName);

        if(foundBeans.size() != 1)
        {
            throw new IllegalStateException(foundBeans.size() + " beans found with name: " + beanName);
        }

        //noinspection unchecked
        return (T)getOrCreateScopedInstanceOfBean(beanManager, foundBeans.iterator().next());
    }

    public static <T> T getOrCreateScopedInstanceOfBeanByClass(Class<T> targetClass, Annotation... qualifier)
    {
        return getOrCreateScopedInstanceOfBeanByClass(BeanManagerProvider.getInstance().getBeanManager(),
                targetClass, qualifier);
    }

    public static <T> T getOrCreateScopedInstanceOfBeanByClass(
            BeanManager beanManager, Class<T> targetClass, Annotation... qualifier)
    {
        return getOrCreateScopedInstanceOfBeanByClass(beanManager, targetClass, false, qualifier);
    }

    public static <T> T getOrCreateScopedInstanceOfBeanByClass(
            Class<T> targetClass, boolean optionalBeanAllowed, Annotation... qualifier)
    {
        return getOrCreateScopedInstanceOfBeanByClass(BeanManagerProvider.getInstance().getBeanManager(),
                targetClass, optionalBeanAllowed, qualifier);
    }

    public static <T> T getOrCreateScopedInstanceOfBeanByClass(
            BeanManager beanManager, Class<T> targetClass, boolean optionalBeanAllowed, Annotation... qualifier)
    {
        Set<? extends Bean> foundBeans = beanManager.getBeans(targetClass, qualifier);

        if(foundBeans.size() > 1)
        {
            StringBuffer detailsOfBeans = new StringBuffer();

            for(Bean bean : foundBeans)
            {
                detailsOfBeans.append(bean.toString());
            }
            throw new IllegalStateException(foundBeans.size() + " beans found for type: " + targetClass.getName() +
                    " the found beans are: " + detailsOfBeans.toString());
        }

        if(foundBeans.size() == 1)
        {
            //noinspection unchecked
            return (T)getOrCreateScopedInstanceOfBean(beanManager, foundBeans.iterator().next());
        }

        if(!optionalBeanAllowed)
        {
            throw new IllegalStateException("No bean found for type: " + targetClass.getName());
        }
        return null;
    }

    public static <T> T getOrCreateScopedInstanceOfBean(Bean<T> bean)
    {
        return getOrCreateScopedInstanceOfBean(BeanManagerProvider.getInstance().getBeanManager(), bean);
    }

    public static <T> T getOrCreateScopedInstanceOfBean(BeanManager beanManager, Bean<T> bean)
    {
        Context context = beanManager.getContext(bean.getScope());

        T result = context.get(bean);

        if (result == null)
        {
            result = context.get(bean, getCreationalContextFor(beanManager, bean));
        }
        return result;
    }

    public static <T> void destroyBean(CreationalContext<T> creationalContext, Bean<T> bean, T beanInstance)
    {
        bean.destroy(beanInstance, creationalContext);
    }

    private static <T> CreationalContext<T> getCreationalContextFor(BeanManager beanManager, Bean<T> bean)
    {
        return beanManager.createCreationalContext(bean);
    }

    /**
     * Load Properties from a configuration file with the given resourceName.
     *
     * @param resourceName
     * @return Properties or <code>null</code> if the given property file doesn't exist
     * @throws IOException on underlying IO problems
     */
    public static Properties getProperties(String resourceName) throws IOException
    {
        Properties props = null;
        ClassLoader cl = ClassUtils.getClassLoader(resourceName);
        InputStream is = cl.getResourceAsStream(resourceName);
        if (is != null)
        {
            props = new Properties();
            props.load(is);
        }

        return props;
    }

    /**
     * Lookup the given property from the default CODI properties file.
     *
     * @param propertyName
     * @return the value of the property or <code>null</code> it it doesn't exist.
     * @throws IOException
     * @throws IllegalArgumentException if the standard CODI properties file couldn't get found
     */
    public static String getCodiProperty(String propertyName) throws IOException
    {
        String value = null;
        Properties props = getProperties(CODI_PROPERTIES);

        if (props != null)
        {
            value = props.getProperty(propertyName);
        }

        return value;
    }

    public static ProjectStage getCurrentProjectStage()
    {
        return getOrCreateScopedInstanceOfBeanByClass(ProjectStage.class);
    }

    public static <T> T tryToInjectDependencies(T instance)
    {
        if(instance == null)
        {
            return null;
        }

        if(instance.getClass().isAnnotationPresent(Advanced.class))
        {
            injectFields(instance);
        }
        return instance;
    }

    /**
     * Performes dependency injection for objects which aren't know as bean
     *
     * @param instance the target instance
     * @param <T> generic type
     * @return an instance produced by the {@link javax.enterprise.inject.spi.BeanManager} or
     * a manually injected instance (or null if the given instance is null)
     */
    @SuppressWarnings({"unchecked"})
    public static <T> T injectFields(T instance)
    {
        if(instance == null)
        {
            return null;
        }

        BeanManager beanManager = BeanManagerProvider.getInstance().getBeanManager();

        T foundBean = (T)getOrCreateScopedInstanceOfBeanByClass(beanManager, instance.getClass(), true);

        if(foundBean != null)
        {
            return foundBean;
        }

        CreationalContext creationalContext = beanManager.createCreationalContext(null);

        AnnotatedType annotatedType = beanManager.createAnnotatedType(instance.getClass());
        InjectionTarget injectionTarget = beanManager.createInjectionTarget(annotatedType);
        injectionTarget.inject(instance, creationalContext);
        return instance;
    }

    /**
     * Checks if the given qualifiers are equal.
     *
     * Qualifiers are equal if they have the same annotationType and all their
     * methods, except those annotated with @Nonbinding, return the same value.
     *
     * @param qualifier1
     * @param qualifier2
     * @return
     */
    public static boolean isQualifierEqual(Annotation qualifier1, Annotation qualifier2)
    {
        Class<? extends Annotation> qualifier1AnnotationType = qualifier1.annotationType();

        // check if the annotationTypes are equal
        if (qualifier1AnnotationType == null || !qualifier1AnnotationType.equals(qualifier2.annotationType()))
        {
            return false;
        }

        // check the values of all qualifier-methods
        // except those annotated with @Nonbinding
        List<Method> bindingQualifierMethods = getBindingQualifierMethods(qualifier1AnnotationType);

        for (Method method : bindingQualifierMethods)
        {
            Object value1 = callMethod(qualifier1, method);
            Object value2 = callMethod(qualifier2, method);

            if (!checkEquality(value1, value2))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Quecks if the two values are equal.
     *
     * @param value1
     * @param value2
     * @return
     */
    private static boolean checkEquality(Object value1, Object value2)
    {
        if ((value1 == null && value2 != null) || (value1 != null && value2 == null))
        {
            return false;
        }

        if (value1 == null && value2 == null)
        {
            return true;
        }

        // now both values are != null

        Class<?> valueClass = value1.getClass();
        
        if (!valueClass.equals(value2.getClass()))
        {
            return false;
        }

        if (valueClass.isPrimitive())
        {
            // primitive types can be checked with ==
            return value1 == value2;
        }
        else if (valueClass.isArray())
        {
            Class<?> arrayType = valueClass.getComponentType();

            if (arrayType.isPrimitive())
            {
                if (Long.TYPE == arrayType)
                {
                    return Arrays.equals(((long[]) value1), (long[]) value2);
                }
                else if (Integer.TYPE == arrayType)
                {
                    return Arrays.equals(((int[]) value1), (int[]) value2);
                }
                else if (Short.TYPE == arrayType)
                {
                    return Arrays.equals(((short[]) value1), (short[]) value2);
                }
                else if (Double.TYPE == arrayType)
                {
                    return Arrays.equals(((double[]) value1), (double[]) value2);
                }
                else if (Float.TYPE == arrayType)
                {
                    return Arrays.equals(((float[]) value1), (float[]) value2);
                }
                else if (Boolean.TYPE == arrayType)
                {
                    return Arrays.equals(((boolean[]) value1), (boolean[]) value2);
                }
                else if (Byte.TYPE == arrayType)
                {
                    return Arrays.equals(((byte[]) value1), (byte[]) value2);
                }
                else if (Character.TYPE == arrayType)
                {
                    return Arrays.equals(((char[]) value1), (char[]) value2);
                }
                return false;
            }
            else
            {
                return Arrays.equals(((Object[]) value1), (Object[]) value2);
            }
        }
        else
        {
            return value1.equals(value2);
        }
    }

    /**
     * Calls the given method on the given instance.
     * Used to determine the values of annotation instances.
     *
     * @param instance
     * @param method
     * @return
     */
    private static Object callMethod(Object instance, Method method)
    {
        boolean accessible = method.isAccessible();

        try
        {
            if (!accessible)
            {
                method.setAccessible(true);
            }

            return method.invoke(instance, EMPTY_OBJECT_ARRAY);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Exception in method call : " + method.getName());
        }
        finally
        {
            // reset accessible value
            method.setAccessible(accessible);
        }
    }

    /**
     * Return a List of all methods of the qualifier,
     * which are not annotated with @Nonbinding.
     * 
     * @param qualifierAnnotationType
     * @return
     */
    private static List<Method> getBindingQualifierMethods(Class<? extends Annotation> qualifierAnnotationType)
    {
        Method[] qualifierMethods = qualifierAnnotationType.getDeclaredMethods();
        if (qualifierMethods.length > 0)
        {
            List<Method> bindingMethods = new ArrayList<Method>();

            for (Method qualifierMethod : qualifierMethods)
            {
                Annotation[] qualifierMethodAnnotations = qualifierMethod.getDeclaredAnnotations();

                if (qualifierMethodAnnotations.length > 0)
                {
                    // look for @Nonbinding
                    boolean nonbinding = false;

                    for (Annotation qualifierMethodAnnotation : qualifierMethodAnnotations)
                    {
                        if (Nonbinding.class.equals(qualifierMethodAnnotation.annotationType()))
                        {
                            nonbinding = true;
                            break;
                        }
                    }

                    if (!nonbinding)
                    {
                        // no @Nonbinding found - add to list
                        bindingMethods.add(qualifierMethod);
                    }
                }
                else
                {
                    // no method-annotations - add to list
                    bindingMethods.add(qualifierMethod);
                }
            }

            return bindingMethods;
        }

        // annotation has no methods
        return Collections.emptyList();
    }
}
