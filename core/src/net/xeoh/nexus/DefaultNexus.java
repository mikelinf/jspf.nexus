/*
 * DefaultKernel.java
 * 
 * Copyright (c) 2011, Ralf Biedert, DFKI. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of the author nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package net.xeoh.nexus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A default implementation of the {@link Nexus} interface. Unless you have specific needs,
 * use this class.
 * 
 * @author Ralf Biedert
 * @since 1.0
 */
public class DefaultNexus implements Nexus {
    /** All services we know */
    final ConcurrentLinkedQueue<Service> services = new ConcurrentLinkedQueue<Service>();

    /** A quick look cache for commons requests */
    final Map<Class<?>, ConcurrentLinkedQueue<Service>> cache = new ConcurrentHashMap<Class<?>, ConcurrentLinkedQueue<Service>>();

    
    /**
     * Returns the appropriate queue for the given class.
     * 
     * @since 1.0
     * @param clazz The class to get the queue for.
     * @return The queue holding the elements.
     */
    private ConcurrentLinkedQueue<Service> queueFor(Class<?> clazz) {
        synchronized (this.cache) {
            // Check if we already have a queue
            final ConcurrentLinkedQueue<Service> queue = this.cache.get(clazz);
            if(queue != null) return queue;
            
            // If not, create it and place it into the cache
            final ConcurrentLinkedQueue<Service> rval = new ConcurrentLinkedQueue<Service>();
            this.cache.put(clazz, rval);
            
            return rval;
        }
    }
    
    /**
     * Tries to locate the service in the given queue. 
     * 
     * @since 1.0
     * @param queue The queue to search in.
     * @param options The options to match. 
     * @return
     */
    private static Service findService(ConcurrentLinkedQueue<Service> queue, Get... options) {
        // If we have no options, just return some element of the queue...
        if(options == null || options.length == 0) return queue.peek();
        
        
        // Now deal with the options ...
        for (Service s : queue) {
            // TODO
            return s;
        }

        // In case we have not found anything, return nothing ...
        return null;
    }
    
    
    
    /*
     * (non-Javadoc)
     * 
     * @see net.jcores.kernel.Kernel#register(net.jcores.kernel.Service)
     */
    public Nexus register(Service service) {
        this.services.add(service);
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.jcores.kernel.Kernel#deregister(net.jcores.kernel.Service)
     */
    public Nexus deregister(Service service) {
        // Remove the object from our main service list
        this.services.remove(service);
        
        // And remove it from all caches. For this we process all queues ...
        for (ConcurrentLinkedQueue<Service> queue : this.cache.values()) {
            // ... and iterate over all queues ...
            final Iterator<Service> iterator = queue.iterator();
            while(iterator.hasNext()) {
                // ... and remove this service.
                final Service next = iterator.next();
                if(next.equals(service)) {
                    iterator.remove();
                }
            }
        }
        
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.jcores.kernel.Kernel#list()
     */
    @Override
    public Collection<Service> list() {
        return new ArrayList<Service>(this.services);
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.jcores.kernel.Kernel#register(java.util.Collection)
     */
    @Override
    public Nexus register(Collection<? extends Service> service) {
        for (Service s : service) {
            register(s);
        }

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.jcores.kernel.Kernel#deregister(java.util.Collection)
     */
    @Override
    public Nexus deregister(Collection<? extends Service> service) {
        for (Service s : service) {
            deregister(s);
        }

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.jcores.kernel.Kernel#get(java.lang.Class, net.jcores.kernel.Kernel.Get[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(Class<T> service, Get... options) {
        final ConcurrentLinkedQueue<Service> queue = queueFor(service);
        final Service selected = findService(queue, options);

        
        // When we have a match, return the candidate. 
        if(selected != null) return (T) selected.getService();
        
        
        // When we have no match, we process the main queue and update this cache ...
        final Service result = findService(this.services, options);
        if(result == null) return null;
        
        // FIXME: In some rare cases services might be added two times to the cache when
        // this passage is entered by two thread at the same time, which, however, should not 
        // be bad, since it won't affect any of our results.
        queue.add(result);
        
        return (T) result.getService();
    }

}
