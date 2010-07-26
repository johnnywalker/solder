package org.jboss.weld.extensions.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.weld.extensions.literal.DefaultLiteral;
import org.jboss.weld.extensions.util.Arrays2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base class for implementing {@link Bean}. The base class is immutable, and
 * uses defensive copying for collections. It uses the defaults from the spec
 * for properties if not specified.
 * 
 * @author Pete Muir
 * 
 */
public abstract class AbstractImmutableBean<T>
{

   private static final Logger log = LoggerFactory.getLogger(AbstractImmutableBean.class);
   
   private final Class<?> beanClass;
   private final String name;
   private final Set<Annotation> qualifiers;
   private final Class<? extends Annotation> scope;
   private final Set<Class<? extends Annotation>> stereotypes;
   private final Set<Type> types;
   private final boolean alternative;
   private final boolean nullable;
   private final Set<InjectionPoint> injectionPoints;

   /**
    * Create a new, immutable bean. All arguments passed as collections are
    * defensively copied.
    * 
    * @param beanClass The Bean class, may not be null
    * @param name The bean name
    * @param qualifiers The bean's qualifiers, if null, a singleton set of
    *           {@link Default} is used
    * @param scope The bean's scope, if null, the default scope of
    *           {@link Dependent} is used
    * @param stereotypes The bean's stereotypes, if null, an empty set is used
    * @param types The bean's types, if null, the beanClass and {@link Object}
    *           will be used
    * @param alternative True if the bean is an alternative
    * @param nullable True if the bean is nullable
    * @param injectionPoints the bean's injection points, if null an empty set
    *           is used
    * @param beanLifecycle Handler for {@link #create(CreationalContext)} and
    *           {@link #destroy(Object, CreationalContext)}
    * 
    * @throws IllegalArgumentException if the beanClass is null
    */
   public AbstractImmutableBean(Class<?> beanClass, String name, Set<Annotation> qualifiers, Class<? extends Annotation> scope, Set<Class<? extends Annotation>> stereotypes, Set<Type> types, boolean alternative, boolean nullable, Set<InjectionPoint> injectionPoints)
   {
      if (beanClass == null)
      {
         throw new IllegalArgumentException("beanClass cannot be null");
      }
      this.beanClass = beanClass;
      this.name = name;
      if (qualifiers == null)
      {
         this.qualifiers = Collections.<Annotation> singleton(DefaultLiteral.INSTANCE);
         log.trace("No qualifers provided for bean class " + beanClass + ", using singleton set of @Default");
      }
      else
      {
         this.qualifiers = new HashSet<Annotation>(qualifiers);
      }
      if (scope == null)
      {
         this.scope = Dependent.class;
         log.trace("No scope provided for bean class " + beanClass + ", using @Dependent");
      }
      else
      {
         this.scope = scope;
      }
      if (stereotypes == null)
      {
         this.stereotypes = Collections.emptySet();
      }
      else
      {
         this.stereotypes = new HashSet<Class<? extends Annotation>>(stereotypes);
      }
      if (types == null)
      {
         this.types = Arrays2.<Type> asSet(Object.class, beanClass);
         log.trace("No types provided for bean class " + beanClass + ", using [java.lang.Object.class, " + beanClass.getName() + ".class]");
      }
      else
      {
         this.types = new HashSet<Type>(types);
      }
      if (injectionPoints == null)
      {
         this.injectionPoints = Collections.emptySet();
      }
      else
      {
         this.injectionPoints = new HashSet<InjectionPoint>(injectionPoints);
      }
      this.alternative = alternative;
      this.nullable = nullable;
   }

   public Class<?> getBeanClass()
   {
      return beanClass;
   }

   public Set<InjectionPoint> getInjectionPoints()
   {
      return injectionPoints;
   }

   public String getName()
   {
      return name;
   }

   public Set<Annotation> getQualifiers()
   {
      return Collections.unmodifiableSet(qualifiers);
   }

   public Class<? extends Annotation> getScope()
   {
      return scope;
   }

   public Set<Class<? extends Annotation>> getStereotypes()
   {
      return Collections.unmodifiableSet(stereotypes);
   }

   public Set<Type> getTypes()
   {
      return Collections.unmodifiableSet(types);
   }

   public boolean isAlternative()
   {
      return alternative;
   }

   public boolean isNullable()
   {
      return nullable;
   }

   @Override
   public String toString()
   {
      return new StringBuilder().append("Custom Bean with bean class ").append(beanClass).append(" and qualifiers ").append(qualifiers).toString();
   }

}