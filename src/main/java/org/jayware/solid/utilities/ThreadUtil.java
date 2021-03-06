/**
 * Solid Utilities -- A collection of utility classes.
 *
 * Copyright (C) 2016 Markus Neubauer <markus.neubauer@jayware.org>,
 *                    Alexander Haumann <alexander.haumann@jayware.org>,
 *                    Manuel Hinke <manuel.hinke@jayware.org>,
 *                    Marina Schilling <marina.schilling@jayware.org>,
 *                    Elmar Schug <elmar.schug@jayware.org>,
 *
 *     This file is part of Solid Utilities.
 *
 *     Solid Utilities is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public License
 *     as published by the Free Software Foundation, either version 3 of
 *     the License, or any later version.
 *
 *     Solid Utilities is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jayware.solid.utilities;


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;


public class ThreadUtil
{
    public static ThreadGroup getRootThreadGroup()
    {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parentThreadGroup;

        while ((parentThreadGroup = threadGroup.getParent()) != null)
        {
            threadGroup = parentThreadGroup;
        }

        return threadGroup;
    }

    public static ThreadGroup[] getAllThreadGroups()
    {
        final ThreadGroup root = getRootThreadGroup();

        int nAlloc = root.activeGroupCount();
        int n = 0;
        ThreadGroup[] groups;

        do
        {
            nAlloc *= 2;
            groups = new ThreadGroup[nAlloc];
            n = root.enumerate(groups, true);
        }
        while (n == nAlloc);

        ThreadGroup[] allGroups = new ThreadGroup[n+1];
        allGroups[0] = root;
        System.arraycopy( groups, 0, allGroups, 1, n );

        return allGroups;
    }

    public static ThreadGroup getThreadGroupByName(String name)
    {
        if (name != null)
        {
            final ThreadGroup[] groups = getAllThreadGroups();
            for (ThreadGroup group : groups)
            {
                if (group.getName().equals(name))
                {
                    return group;
                }
            }
        }

        return null;
    }

    public static Thread[] getAllThreadsInGroup(String groupName)
    {
        return getAllThreadsInGroup(getThreadGroupByName(groupName));
    }

    public static Thread[] getAllThreadsInGroup(ThreadGroup group)
    {
        if (group != null)
        {
            int nAlloc = group.activeCount();
            int n = 0;
            Thread[] threads;

            do
            {
                nAlloc *= 2;
                threads = new Thread[nAlloc];
                n = group.enumerate(threads);
            }
            while (n == nAlloc);

            return java.util.Arrays.copyOf(threads, n);
        }

        return new Thread[0];
    }

    public static Thread[] getAllThreads()
    {
        final ThreadGroup rootThreadGroup = getRootThreadGroup();
        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        int nAlloc = threadMXBean.getThreadCount();
        int n = 0;
        Thread[] threads;

        do
        {
            nAlloc *= 2;
            threads = new Thread[nAlloc];
            n = rootThreadGroup.enumerate(threads, true);
        }
        while ( n == nAlloc );

        return java.util.Arrays.copyOf(threads, n);
    }

    public static Thread getThreadByName(String name)
    {
        if (name != null)
        {
            final Thread[] threads = getAllThreads();

            for (Thread thread : threads)
            {
                if (thread.getName().equals(name))
                {
                    return thread;
                }
            }
        }

        return null;
    }

    public static boolean isThreadInGroup(Thread thread, ThreadGroup group)
    {
        return thread.getThreadGroup() == group;
    }
}
