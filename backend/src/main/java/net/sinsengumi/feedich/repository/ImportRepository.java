package net.sinsengumi.feedich.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import net.sinsengumi.feedich.model.Import;
import net.sinsengumi.feedich.model.Import.ImportStatus;

@Repository
public interface ImportRepository {

    int create(Import import_);

    int updateStatus(@Param("id") int id, @Param("status") ImportStatus status);

    Import getLatestImport(int userId);
}
