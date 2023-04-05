package com.smartmanager.dao;

import com.smartmanager.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {
    //Pageable-> current page, contact perpage
   @Query("from Contact as c where c.user.id=:userId")
    public Page<Contact> findContactsByUser(@Param("userId") int userId, Pageable pageable);
}
