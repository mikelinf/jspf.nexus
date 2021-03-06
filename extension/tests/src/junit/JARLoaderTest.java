/*
 * JARLoaderTest.java
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
package junit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.xeoh.nexus.JARLocator;
import net.xeoh.nexus.Service;
import net.xeoh.nexus.URILoader;

import org.junit.Test;

/**
 * @author Ralf Biedert
 * @since 1.0
 */
public class JARLoaderTest {
    @Test
    public void testFind() {
        final Collection<String> result = new ArrayList<String>();
        final URILoader locator = new JARLocator(new File("extension/tests/data/coolplugin.jar")) {
            /*
             * (non-Javadoc)
             * 
             * @see net.xeoh.nexus.JARLocator#locate()
             */
            @Override
            public Collection<Service> locate() {
                try {
                    result.addAll(listAll(this.uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return super.locate();
            }
        };

        Collection<Service> locate = locator.locate();
        for (String string : result) {
            System.out.println(string);
        }

    }
}
