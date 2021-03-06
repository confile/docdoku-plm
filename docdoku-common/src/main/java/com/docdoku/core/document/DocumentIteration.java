/*
 * DocDoku, Professional Open Source
 * Copyright 2006 - 2014 DocDoku SARL
 *
 * This file is part of DocDokuPLM.
 *
 * DocDokuPLM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DocDokuPLM is distributed in the hope that it will be useful,  
 * but WITHOUT ANY WARRANTY; without even the implied warranty of  
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the  
 * GNU Affero General Public License for more details.  
 *  
 * You should have received a copy of the GNU Affero General Public License  
 * along with DocDokuPLM.  If not, see <http://www.gnu.org/licenses/>.  
 */
package com.docdoku.core.document;

import com.docdoku.core.common.BinaryResource;
import com.docdoku.core.common.FileHolder;
import com.docdoku.core.common.User;
import com.docdoku.core.meta.InstanceAttribute;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.*;

/**
 * This <a href="DocumentIteration.html">DocumentIteration</a>
 * class represents the iterated part of a document.
 * The iteration attribute indicates the order in which the modifications
 * have been made on the document.
 * 
 * @author Florent Garin
 * @version 1.0, 02/06/08
 * @since   V1.0
 */
@Table(name = "DOCUMENTITERATION")
@javax.persistence.IdClass(com.docdoku.core.document.DocumentIterationKey.class)
@NamedQueries ({
    @NamedQuery(name="DocumentIteration.findLinks", query = "SELECT l FROM DocumentLink l WHERE l.targetDocument = :target"),
    @NamedQuery(name="DocumentIteration.findByBinaryResource", query = "SELECT d FROM DocumentIteration d WHERE :binaryResource member of d.attachedFiles")
})
@javax.persistence.Entity
public class DocumentIteration implements Serializable, FileHolder, Comparable<DocumentIteration>, Cloneable {

    @Id
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "DOCUMENTMASTER_ID", referencedColumnName = "DOCUMENTMASTER_ID"),
        @JoinColumn(name = "DOCUMENTREVISION_VERSION", referencedColumnName = "VERSION"),
        @JoinColumn(name = "WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private DocumentRevision documentRevision;

    @javax.persistence.Id
    private int iteration;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "DOCUMENTITERATION_BINRES",
    inverseJoinColumns = {
        @JoinColumn(name = "ATTACHEDFILE_FULLNAME", referencedColumnName = "FULLNAME")
    },
    joinColumns = {
        @JoinColumn(name = "WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID"),
        @JoinColumn(name = "DOCUMENTMASTER_ID", referencedColumnName = "DOCUMENTMASTER_ID"),
        @JoinColumn(name = "DOCUMENTREVISION_VERSION", referencedColumnName = "DOCUMENTREVISION_VERSION"),
        @JoinColumn(name = "ITERATION", referencedColumnName = "ITERATION")
    })
    private Set<BinaryResource> attachedFiles = new HashSet<>();
    private String revisionNote;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "AUTHOR_LOGIN", referencedColumnName = "LOGIN"),
        @JoinColumn(name = "AUTHOR_WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID")
    })
    private User author;
    
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "DOCUMENTITERATION_DOCUMENTLINK",
    inverseJoinColumns = {
        @JoinColumn(name = "DOCUMENTLINK_ID", referencedColumnName = "ID")
    },
    joinColumns = {
        @JoinColumn(name = "WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID"),
        @JoinColumn(name = "DOCUMENTMASTER_ID", referencedColumnName = "DOCUMENTMASTER_ID"),
        @JoinColumn(name = "DOCUMENTREVISION_VERSION", referencedColumnName = "DOCUMENTREVISION_VERSION"),
        @JoinColumn(name = "ITERATION", referencedColumnName = "ITERATION")
    })
    private Set<DocumentLink> linkedDocuments = new HashSet<>();
    
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKey(name = "name")
    @JoinTable(name = "DOCUMENTITERATION_ATTRIBUTE",
    inverseJoinColumns = {
        @JoinColumn(name = "INSTANCEATTRIBUTE_ID", referencedColumnName = "ID")
    },
    joinColumns = {
        @JoinColumn(name = "WORKSPACE_ID", referencedColumnName = "WORKSPACE_ID"),
        @JoinColumn(name = "DOCUMENTMASTER_ID", referencedColumnName = "DOCUMENTMASTER_ID"),
        @JoinColumn(name = "DOCUMENTREVISION_VERSION", referencedColumnName = "DOCUMENTREVISION_VERSION"),
        @JoinColumn(name = "ITERATION", referencedColumnName = "ITERATION")
    })
    private Map<String, InstanceAttribute> instanceAttributes = new HashMap<>();

    public DocumentIteration() {
    }

    public DocumentIteration(DocumentRevision pDocumentRevision, int pIteration, User pAuthor) {
        setDocumentRevision(pDocumentRevision);
        iteration = pIteration;
        author = pAuthor;
    }

    public void setDocumentRevision(DocumentRevision documentRevision) {
        this.documentRevision = documentRevision;
    }


    public String getWorkspaceId() {
        return documentRevision==null?"":documentRevision.getWorkspaceId();
    }

    public String getId() {
        return documentRevision==null?"":documentRevision.getId();
    }

    public String getDocumentVersion() {
        return documentRevision==null?"":documentRevision.getVersion();
    }

    public String getDocumentMasterId() {
        return getId();
    }

    public String getDocumentRevisionVersion(){
        return getDocumentVersion();
    }

    public int getIteration() {
        return iteration;
    }

    public void setRevisionNote(String pRevisionNote) {
        revisionNote = pRevisionNote;
    }

    public String getRevisionNote() {
        return revisionNote;
    }

    public void setAttachedFiles(Set<BinaryResource> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public boolean removeFile(BinaryResource pBinaryResource) {
        return attachedFiles.remove(pBinaryResource);
    }

    public void addFile(BinaryResource pBinaryResource) {
        attachedFiles.add(pBinaryResource);
    }

    @Override
    public Set<BinaryResource> getAttachedFiles() {
        return attachedFiles;
    }

    public DocumentRevisionKey getDocumentRevisionKey() {
        return documentRevision==null?new DocumentRevisionKey(new DocumentMasterKey("",""),""):documentRevision.getKey();
    }
    public DocumentIterationKey getKey() {
        return new DocumentIterationKey(getDocumentRevisionKey(),iteration);
    }


    public void setAuthor(User pAuthor) {
        author = pAuthor;
    }

    public User getAuthor() {
        return author;
    }

    @XmlTransient
    public DocumentRevision getDocumentRevision() {
        return documentRevision;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = (creationDate!=null) ? (Date) creationDate.clone() : null;
    }

    public Date getCreationDate() {
        return (creationDate!=null) ? (Date) creationDate.clone() : null;
    }

    public Set<DocumentLink> getLinkedDocuments() {
        return linkedDocuments;
    }

    public void setLinkedDocuments(Set<DocumentLink> pLinkedDocuments) {
        linkedDocuments=pLinkedDocuments;
    }

    public Map<String, InstanceAttribute> getInstanceAttributes() {
        return instanceAttributes;
    }

    public void setInstanceAttributes(Map<String, InstanceAttribute> pInstanceAttributes) {
        instanceAttributes=pInstanceAttributes;
    }

    @Override
    public String toString() {
        return documentRevision + "-" + iteration;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + getWorkspaceId().hashCode();
        hash = 31 * hash + getId().hashCode();
        hash = 31 * hash + getDocumentVersion().hashCode();
        hash = 31 * hash + iteration;
        return hash;
    }

    @Override
    public boolean equals(Object pObj) {
        if (this == pObj) {
            return true;
        }
        if (!(pObj instanceof DocumentIteration)) {
            return false;
        }
        DocumentIteration docI = (DocumentIteration) pObj;
        return docI.getId().equals(getId()) &&
                docI.getWorkspaceId().equals(getWorkspaceId()) &&
                docI.getDocumentVersion().equals(getDocumentVersion()) &&
                docI.iteration==iteration;
    }




    @Override
    public int compareTo(DocumentIteration pDoc) {

        int wksComp = getWorkspaceId().compareTo(pDoc.getWorkspaceId());
        if (wksComp != 0) {
            return wksComp;
        }
        int docmIdComp = getId().compareTo(pDoc.getId());
        if (docmIdComp != 0) {
            return docmIdComp;
        }
        int docmVersionComp = getDocumentVersion().compareTo(pDoc.getDocumentVersion());
        if (docmVersionComp != 0) {
            return docmVersionComp;
        } else {
            return iteration - pDoc.iteration;
        }
    }

    /**
     * perform a deep clone operation
     */
    @Override
    public DocumentIteration clone() {
        DocumentIteration clone = null;
        try {
            clone = (DocumentIteration) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        //perform a deep copy
        clone.attachedFiles = new HashSet<>(attachedFiles);

        Set<DocumentLink> clonedLinks = new HashSet<>();
        for (DocumentLink link : linkedDocuments) {
            DocumentLink clonedLink = link.clone();
            clonedLinks.add(clonedLink);
        }
        clone.linkedDocuments = clonedLinks;

        //perform a deep copy
        Map<String, InstanceAttribute> clonedInstanceAttributes = new HashMap<>();
        for (InstanceAttribute attribute : instanceAttributes.values()) {
            InstanceAttribute clonedAttribute = attribute.clone();
            clonedInstanceAttributes.put(clonedAttribute.getName(), clonedAttribute);
        }
        clone.instanceAttributes = clonedInstanceAttributes;

        if (creationDate != null) {
            clone.creationDate = (Date) creationDate.clone();
        }
        return clone;
    }
}