package com.philcode.springboot.transactional.repository;

import com.philcode.springboot.transactional.entity.UserAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAssetRepository extends JpaRepository<UserAsset, Long> {

    /**
     * 增加余额
     *
     * @param accountId 账号ID
     * @param money 金额
     * @param assetStatus 资产状态
     * @return 更新操作影响的记录数，0未更新成功
     */
    @Modifying
    @Query("UPDATE UserAsset a SET a.balance = a.balance + :money " +
            "WHERE a.accountId = :accountId AND a.status = :status")
    int increase(@Param("accountId") long accountId, @Param("money") int money, @Param("status") int assetStatus);

    /**
     * 减少余额
     *
     * @param accountId 账号ID
     * @param money 金额
     * @param assetStatus 资产状态
     * @return 更新操作影响的记录数，0未更新成功
     */
    @Modifying
    @Query("UPDATE UserAsset a SET a.balance = a.balance - :money " +
            "WHERE a.accountId = :accountId AND a.balance >= :money AND a.status = :status")
    int decrease(@Param("accountId") long accountId, @Param("money") int money, @Param("status") int assetStatus);

    /**
     * 按账号ID 查询
     *
     * @param accountId 账号
     * @return Optional<UserAsset>
     */
    Optional<UserAsset> findByAccountId(Long accountId);
}
