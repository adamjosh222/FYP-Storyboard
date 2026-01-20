/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "supervisors")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Supervisors.findAll", query = "SELECT s FROM Supervisors s")
    , @NamedQuery(name = "Supervisors.findBySupervisorID", query = "SELECT s FROM Supervisors s WHERE s.supervisorID = :supervisorID")
    , @NamedQuery(name = "Supervisors.findBySvName", query = "SELECT s FROM Supervisors s WHERE s.svName = :svName")
    , @NamedQuery(name = "Supervisors.findBySvEmail", query = "SELECT s FROM Supervisors s WHERE s.svEmail = :svEmail")
    , @NamedQuery(name = "Supervisors.findByPhoneNum", query = "SELECT s FROM Supervisors s WHERE s.phoneNum = :phoneNum")
    , @NamedQuery(name = "Supervisors.findByDepartment", query = "SELECT s FROM Supervisors s WHERE s.department = :department")
    , @NamedQuery(name = "Supervisors.findByRoomNo", query = "SELECT s FROM Supervisors s WHERE s.roomNo = :roomNo")})
public class Supervisors implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "supervisorID")
    private Integer supervisorID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "sv_name")
    private String svName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "sv_email")
    private String svEmail;
    @Size(max = 20)
    @Column(name = "phoneNum")
    private String phoneNum;
    @Size(max = 100)
    @Column(name = "department")
    private String department;
    @Size(max = 50)
    @Column(name = "roomNo")
    private String roomNo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supervisorID")
    private Collection<Feedbacks> feedbacksCollection;
    @OneToMany(mappedBy = "supervisorID")
    private Collection<Students> studentsCollection;
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    @ManyToOne(optional = false)
    private Users userID;

    public Supervisors() {
    }

    public Supervisors(Integer supervisorID) {
        this.supervisorID = supervisorID;
    }

    public Supervisors(Integer supervisorID, String svName, String svEmail) {
        this.supervisorID = supervisorID;
        this.svName = svName;
        this.svEmail = svEmail;
    }

    public Integer getSupervisorID() {
        return supervisorID;
    }

    public void setSupervisorID(Integer supervisorID) {
        this.supervisorID = supervisorID;
    }

    public String getSvName() {
        return svName;
    }

    public void setSvName(String svName) {
        this.svName = svName;
    }

    public String getSvEmail() {
        return svEmail;
    }

    public void setSvEmail(String svEmail) {
        this.svEmail = svEmail;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    @XmlTransient
    public Collection<Feedbacks> getFeedbacksCollection() {
        return feedbacksCollection;
    }

    public void setFeedbacksCollection(Collection<Feedbacks> feedbacksCollection) {
        this.feedbacksCollection = feedbacksCollection;
    }

    @XmlTransient
    public Collection<Students> getStudentsCollection() {
        return studentsCollection;
    }

    public void setStudentsCollection(Collection<Students> studentsCollection) {
        this.studentsCollection = studentsCollection;
    }

    public Users getUserID() {
        return userID;
    }

    public void setUserID(Users userID) {
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (supervisorID != null ? supervisorID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Supervisors)) {
            return false;
        }
        Supervisors other = (Supervisors) object;
        if ((this.supervisorID == null && other.supervisorID != null) || (this.supervisorID != null && !this.supervisorID.equals(other.supervisorID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.entity.Supervisors[ supervisorID=" + supervisorID + " ]";
    }
    
}
