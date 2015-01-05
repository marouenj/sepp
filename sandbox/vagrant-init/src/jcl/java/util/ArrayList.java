package java.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//import java.lang.reflect.Array;

public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable
{

  private static final long serialVersionUID = 8683452581122892189L;

  private final int objId = sepp.jcl.IdGen.getId();

  public ArrayList()
  {
  }

  public boolean add(E e)
  {
    Class<?>[] paramTypes = new Class<?>[1];
    paramTypes[0] = Object.class;

    sepp.jcl.rmi.client.Wildcard<Boolean> task = new sepp.jcl.rmi.client.Wildcard<Boolean>(objId, "java.util.ArrayList", "add", paramTypes);
    task.setArg(e);

    Boolean rez = null;
    try
    {
      rez = sepp.jcl.SingletonCompute.executeTask(task);
    }
    catch (java.rmi.RemoteException ex)
    {
      ex.printStackTrace();
    }

    if (rez == null || !rez)
      return false;
    
    return true;
  }

  public void add(int index, E e)
  {
    Class<?>[] paramTypes = new Class<?>[2];
    paramTypes[0] = Object.class;
    paramTypes[1] = Object.class;

    sepp.jcl.rmi.client.Wildcard<Boolean> task = new sepp.jcl.rmi.client.Wildcard<Boolean>(objId, "java.util.ArrayList", "add", paramTypes);
    task.setArg(index);
    task.setArg(e);

    try
    {
      sepp.jcl.SingletonCompute.executeTask(task);
    }
    catch (java.rmi.RemoteException ex)
    {
      ex.printStackTrace();
    }
  }

  public int size()
  {
    Class<?>[] paramTypes = new Class<?>[0];

    sepp.jcl.rmi.client.Wildcard<Integer> task = new sepp.jcl.rmi.client.Wildcard<Integer>(objId, "java.util.ArrayList", "size", paramTypes);

    Integer rez = null;
    try
    {
      rez = sepp.jcl.SingletonCompute.executeTask(task);
    }
    catch (java.rmi.RemoteException ex)
    {
      ex.printStackTrace();
    }

    if (rez == null)
      return -1;

    return rez;
  }










  public ArrayList(int capacity)
  {
  }

  public ArrayList(Collection<? extends E> c)
  {
  }

  public void trimToSize()
  {
  }

  public void ensureCapacity(int minCapacity)
  {
  }

  public boolean isEmpty()
  {
    return false;
  }

  public boolean contains(Object e)
  {
    return false;
  }

  public int indexOf(Object e)
  {
    return -1;
  }

  public int lastIndexOf(Object e)
  {
    return -1;
  }

  public Object clone()
  {
    return null;
  }

  public Object[] toArray()
  {
    return null;
  }

  public <T> T[] toArray(T[] a)
  {
    return null;
  }

  public E get(int index)
  {
    return null;
  }

  public E set(int index, E e)
  {
    return null;
  }

  public E remove(int index)
  {
    return null;
  }

  public void clear()
  {
  }

  public boolean addAll(Collection<? extends E> c)
  {
    return false;
  }

  public boolean addAll(int index, Collection<? extends E> c)
  {
    return false;
  }

  protected void removeRange(int fromIndex, int toIndex)
  {
  }

  boolean removeAllInternal(Collection<?> c)
  {
    return false;
  }

  boolean retainAllInternal(Collection<?> c)
  {
    return false;
  }

  private void writeObject(ObjectOutputStream s) throws IOException
  {
  }

  private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException
  {
  }
}
