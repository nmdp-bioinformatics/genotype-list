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

/**
 * Fluent builder-style client API for creating GL String-formatted representations of gl resources.
 */
public final class GlstringBuilder {
    // todo:  replace this hack with antlr grammar
    private String locus;
    private Tree tree = new Tree();


    /**
     * Return this GL String builder configured with the specified locus.  If an allele has
     * already been added to this builder, this call provides the locus operator ('<code>^</code>' character).
     *
     * @param glstring locus in GL String format, must not be null
     * @return this GL String builder configured with the specified locus
     */
    public GlstringBuilder locus(final String glstring) {
        if (tree.isEmpty()) {
            locus = glstring;
        }
        else {
            Node root = new Node();
            root.value = "^";
            root.left = tree.root;
            tree.root = root;
        }
        return this;
    }

    /**
     * Return this GL String builder configured with the specified allele. Calls to this method must
     * be interspersed by calls to operator methods ({@link #allicAmbiguity()}, {@link #inPhase()},
     * {@link #xxx()}, {@link #genotypicAmbiguity()}, and {@link #locus(String)}).
     *
     * @param glstring allele in GL String format, must not be null
     * @return this GL String builder configured with the specified allele
     */
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

    /**
     * Return this GL String builder configured with an allelic ambiguity operator ('<code>/</code>' character).
     *
     * @return this GL String builder configured with an allelic ambiguity operator ('<code>/</code>' character)
     */
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

    /**
     * Return this GL String builder configured with an in phase operator ('<code>~</code>' character).
     *
     * @return this GL String builder configured with an in phase operator ('<code>~</code>' character)
     */
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

    /**
     * Return this GL String builder configured with an xxx operator ('<code>+</code>' character).
     *
     * @return this GL String builder configured with an xxx operator ('<code>+</code>' character)
     */
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

    /**
     * Return this GL String builder configured with a genotypic ambiguity operator ('<code>|</code>' character).
     *
     * @return this GL String builder configured with a genotypic ambiguity operator ('<code>|</code>' character)
     */
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

    /**
     * Return this GL String builder with its configuration reset.
     *
     * @return this GL String builder with its configuration reset
     */
    public GlstringBuilder reset() {
        tree.root = null;
        return this;
    }

    /**
     * Build and return a new GL String configured from the properties of this GL String builder.
     *
     * @return a new GL String configured from the properties of this GL String builder
     */
    public String build() {
        if (locus == null && tree.isEmpty()) {
            throw new IllegalStateException("must call locus(String) or allele(String) at least once");
        }
        return tree.isEmpty() ? locus : tree.toString();
    }

    private final class Tree {
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

    private final class Node {
        Node left;
        Node right;
        String value;

        boolean isLeaf() {
          return left == null && right == null;
        }
    }
}