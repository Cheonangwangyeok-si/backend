package com.likelion.mindiary.domain.diary.repository;

import com.likelion.mindiary.domain.diary.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary,Long> {

}
