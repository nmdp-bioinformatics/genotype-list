/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
    Copyright (c) 2012 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

*/
package org.immunogenomics.gl.client;

import java.util.Collection;
import java.util.HashSet;

/**
 * Fluent builder-style client API for ... GL String format.
 */
public final class GlstringBuilder {
    // todo:  replace this hack with antlr grammar
    private String locus;
    private Tree tree = new Tree();

    public GlstringBuilder locus(final String glstring) {
        if (tree.isEmpty())
        {
            locus = glstring;
        }
        else
        {
            Node root = new Node();
            root.value = "^";
            root.left = tree.root;
            tree.root = root;
        }
        return this;
    }

    public GlstringBuilder allele(final String glstring) {
        Node node = new Node();
        node.value = glstring;
        if (tree.isEmpty()) {
            tree.root = node;
        }
        else {
            if (tree.root.left == null) {
                tree.root.left = node;
            }
            else if (tree.root.right == null) {
                tree.root.right = node;
            }
            else {
                throw new IllegalStateException("must provide an operator(allelicAmbiguity, inPhase, xxx, genotypicAmbiguity, locus) between successive calls to allele(glstring)");
            }
        }
        return this;
    }

    public GlstringBuilder allelicAmbiguity() {
        if (tree.isEmpty()) {
            throw new IllegalStateException("must call allele(String) before any operator (allelicAmbiguity, inPhase, xxx, genotypicAmbiguity, locus)");
        }
        Node root = new Node();
        root.value = "/";
        root.left = tree.root;
        tree.root = root;
        return this;
    }

    public GlstringBuilder inPhase() {
        if (tree.isEmpty()) {
            throw new IllegalStateException("must call allele(String) before any operator (allelicAmbiguity, inPhase, xxx, genotypicAmbiguity, locus)");
        }
        Node root = new Node();
        root.value = "~";
        root.left = tree.root;
        tree.root = root;
        return this;
    }

    public GlstringBuilder xxx() {
        if (tree.isEmpty()) {
            throw new IllegalStateException("must call allele(String) before any operator (allelicAmbiguity, inPhase, xxx, genotypicAmbiguity, locus)");
        }
        Node root = new Node();
        root.value = "+";
        root.left = tree.root;
        tree.root = root;
        return this;
    }

    public GlstringBuilder genotypicAmbiguity() {
        if (tree.isEmpty()) {
            throw new IllegalStateException("must call allele(String) before any operator (allelicAmbiguity, inPhase, xxx, genotypicAmbiguity, locus)");
        }
        Node root = new Node();
        root.value = "|";
        root.left = tree.root;
        tree.root = root;
        return this;
    }

    public GlstringBuilder reset() {
        tree.root = null;
        return this;
    }

    public String build() {
        if (locus == null && tree.isEmpty()) {
            throw new IllegalStateException("must call locus(String) or allele(String) at least once");
        }
        return tree.isEmpty() ? locus : tree.toString();
    }

    final class Tree {
        Node root;

        boolean isEmpty() {
            return root == null;
        }

        private void dfs(final Node node, final StringBuilder sb) {
            if (node.left != null) {
                dfs(node.left, sb);
            }
            sb.append(node.value);
            if (node.right != null) {
                dfs(node.right, sb);
            }
        }

        @Override
        public String toString() {
            if (root == null) {
                return "";
            }
            if (root.isLeaf()) {
                return root.value;
            }

            StringBuilder sb = new StringBuilder();
            dfs(root, sb);
            // todo:  trim last character if ends with operator
            return sb.toString();
        }
    }

    final class Node {
        Node left;
        Node right;
        String value;

        boolean isLeaf() {
          return left == null && right == null;
        }
    }
}