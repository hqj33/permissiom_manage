#sql("findUserByCond")
SELECT * FROM `system_user` WHERE 1=1
    #if(null != username)
        AND `user` LIKE CONCAT('%',#para(username),'%')
    #end
    #if(null != createDate)
        AND `create_date` = #para(createDate)
    #end
    #if(null != status)
        AND `status` = #para(status)
    #end
#end

#sql("findListUserByIds")
SELECT * FROM `system_user` WHERE id IN (
    #for(id:ids)
         #(for.index > 0 ? "," : "")#(id)
    #end
)
#end