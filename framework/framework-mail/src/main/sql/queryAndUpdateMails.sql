/*
 * 查询数据库中的邮件,并将查出的邮件状态改为已删除(4),返回查出的邮件
 * 参数:maxRow - 最多返回的行数
 * $Id: queryAndUpdateMails.sql 2439 2011-01-19 05:37:35Z 100529 $
 */
drop PROCEDURE if exists queryAndUpdateMails;
--查询未发送或者发送失败的前1000条邮件,并将查询出的邮件状态改为已删除
CREATE PROCEDURE queryAndUpdateMails(IN maxRow int)
BEGIN
BEGIN
		declare iStop int default 0;
		declare pMailId int;
		declare pMailTo varchar(500);
		declare pMailCc varchar(500);
		declare pMailBcc varchar(500);
		declare pTitle varchar(500);
		declare pContent varchar(500);
		declare pIsMime tinyint(1);
		declare pSendTime DateTime;
		declare rows int;
		declare  curMails cursor for
		select mailId,	mailTo,	mailCc,	mailBcc,	title,	content,	isMime,	sendTime 
			from Mails where status=0 or status=2 and sendTime<=now() order by status,sendTime;

		declare CONTINUE HANDLER FOR NOT FOUND SET iStop=1;

		drop TEMPORARY table if exists tmp_mails;
		CREATE TEMPORARY table tmp_mails
		select mailId,	mailTo,	mailCc,	mailBcc,	title,	content,	isMime,	sendTime 
			from Mails where 1=2;
		open curMails;

		fetch curMails into pMailId,	pMailTo,	pMailCc,	pMailBcc,	pTitle,	pContent,	pIsMime,	pSendTime;   
		set rows=1;
    -- 这个就是判断是否游标已经到达了最后   
    while (iStop <> 1) && (rows<=maxRow) do  
				insert into tmp_mails(mailId,	mailTo,	mailCc,	mailBcc,	title,	content,	isMime,	sendTime)
					values(pMailId,	pMailTo,	pMailCc,	pMailBcc,	pTitle,	pContent,	pIsMime,	pSendTime);

        -- 读取下一行的数据    
        fetch curMails into pMailId,	pMailTo,	pMailCc,	pMailBcc,	pTitle,	pContent,	pIsMime,	pSendTime;   

				set rows=rows + 1;
    end while;  -- 循环结束   
    close curMails; -- 关闭游标   

		UPDATE Mails SET status=4 WHERE mailId in(select mailId from tmp_mails);
		select * from tmp_mails;
END
