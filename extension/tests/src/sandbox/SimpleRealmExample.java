/*
 * SimpleRealmExample.java
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
package sandbox;

import java.io.File;
import java.util.Collection;

import net.xeoh.nexus.Blacklist;
import net.xeoh.nexus.Candidate;
import net.xeoh.nexus.DefaultNexus;
import net.xeoh.nexus.FilterRule;
import net.xeoh.nexus.JARLocator;
import net.xeoh.nexus.Nexus;
import net.xeoh.nexus.Service;


/**
 * @author Ralf Biedert
 * @since 1.0
 * 
 */
public class SimpleRealmExample {
    public static void main(String[] args) {
        /*
        final Nexus nexus = new DefaultNexus();
        final Locator locator = new JARLocator("x.jar", "myrealm"); // do we need a realm here or just do that inside?
        final Locator locator = new ClassPathFolderLocator("bin/", "myrealm");
        final Locator locator = new ScanningLocator("plugins/", "myrealm");
        
        
        nexus.register(locator.locate());
        */

        final Nexus nexus = new DefaultNexus();
        final JARLocator locator = new JARLocator(new File("x.jar")); // do we need a realm here or just do that inside?
        final Blacklist blacklist = new Blacklist(FilterRule.NAME("net.xeoh.x"));
        
        final Collection<Candidate> candidates = blacklist.filter(locator.candidates());
        final Collection<Service> locate = locator.locate(candidates);
        
        /*
        
        // final DependencyProcessor dp = new DependencyProcessor(nexus);
        final AbstractAnnotationProcessor jspfProcessor = (AbstractAnnotationProcessor) new Object(); {
            final AbstractAnnotationProcessor disabler = (AbstractAnnotationProcessor) new Object();            
            final AbstractAnnotationProcessor dependencies = (AbstractAnnotationProcessor) new Object();
            final AbstractAnnotationProcessor capabilities = (AbstractAnnotationProcessor) new Object();
            final AbstractAnnotationProcessor threads = (AbstractAnnotationProcessor) new Object();
            final AbstractAnnotationProcessor timers = (AbstractAnnotationProcessor) new Object();
            
            disabler.process(newspan)
            capabilities.process(newspan);
            
            // **ALL** services need to be processed (we might have inactive ones)
            dependencies.process(nexus.list());
            
            // ...
            
            // Now when we have all dependencies, we need to add the plugin to the nexus 
            nexus.add(newspawn)
            
            // next we can process the 
            
        }
        
        locator.whitelist(null);
        */
        final Collection<Service> newspawn = locator.locate();
        
    }
}
