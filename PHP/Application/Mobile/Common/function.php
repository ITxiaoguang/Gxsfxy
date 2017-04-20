<?php

//输出数据
function outputData($data)
{
    $new_data = array();
    $new_data['code'] = 200;
    $new_data['data'] = $data;
    echo json_encode($new_data);
    $logModel = new \Mobile\Model\LogModel();
    $logModel->insert(I('post.model'), I('post.control'), I('post.action'), 'Success', json_encode($new_data), I('post.client'), I('post.version'), I('post.device'), I('post.time'));
    die;
}

//输出错误
function outputError($message)
{
    //输出数据
    $new_data = array();
    $new_data['code'] = 200;
    $new_data['error'] = $message;
    echo json_encode($new_data);
    $logModel = new \Mobile\Model\LogModel();
    $logModel->insert(I('post.model'), I('post.control'), I('post.action'), 'Failure', json_encode($new_data), I('post.client'), I('post.version'), I('post.device'), I('post.time'));
    die;
}

//发送邮件
function sendMail($to, $title, $content)
{
    Vendor('PHPMailer.PHPMailerAutoload');
    $mail = new PHPMailer(); //实例化
    $mail->IsSMTP(); // 启用SMTP
    $mail->Host = C('MAIL_HOST'); //smtp服务器的名称（这里以QQ邮箱为例）
    $mail->SMTPAuth = C('MAIL_SMTPAUTH'); //启用smtp认证
    $mail->Username = C('MAIL_USERNAME'); //你的邮箱名
    $mail->Password = C('MAIL_PASSWORD'); //邮箱密码
    $mail->From = C('MAIL_FROM'); //发件人地址（也就是你的邮箱地址）
    $mail->FromName = C('MAIL_FROMNAME'); //发件人姓名
    $mail->AddAddress($to, "尊敬的客户");
    $mail->WordWrap = 50; //设置每行字符长度
    $mail->IsHTML(C('MAIL_ISHTML')); // 是否HTML格式邮件
    $mail->CharSet = C('MAIL_CHARSET'); //设置邮件编码
    $mail->Subject = $title; //邮件主题
    $mail->Body = $content; //邮件内容
    $mail->AltBody = "邮件正文不支持HTML"; //邮件正文不支持HTML的备用显示
    return ($mail->Send());
}

//发送通知 - 向所有IOS设备
function sendNotifyIosAll($message, $data)
{
    if (empty($message)) {
        return;
    }

    require_once("JPush/JPush.php");
    $client = new \JPush('5f1fab3af9a9e2c85ec0e637', 'e0be4ccde587e4502852edfe');
    $client->push()
        ->setPlatform('ios')
        ->addAllAudience()
        ->addIosNotification($message, '广西师范学院', 1, $data)
        ->send();
}

//发送消息 - 向指定的 RegistrationId
function sendMessageByPushId($push_id, $data)
{
    if (empty($push_id)) {
        return;
    }

    require_once("JPush/JPush.php");
    $client = new \JPush('5f1fab3af9a9e2c85ec0e637', 'e0be4ccde587e4502852edfe');
    $client->push()
        ->setPlatform('ios', 'android')
        ->addRegistrationId($push_id)
        ->setMessage('message', 'title', 'type', $data)
        ->setOptions(100000, 3600, null, false)
        ->send();

}

//发送通知 - 向所有安卓设备
function sendNotifyAndroidAll($message, $data)
{
    if (empty($message)) {
        return;
    }

    require_once("JPush/JPush.php");
    $client = new \JPush('5f1fab3af9a9e2c85ec0e637', 'e0be4ccde587e4502852edfe');
    $client->push()
        ->setPlatform('android')
        ->addAllAudience()
        ->addAndroidNotification($message, '广西师范学院', 1, $data)
        ->send();
}

//发送通知 - 向 Android 指定的 RegistrationId
function sendNotifyIosByPushId($push_id, $message, $data)
{
    if (empty($push_id) || empty($message)) {
        return;
    }

    require_once("JPush/JPush.php");
    $client = new \JPush('5f1fab3af9a9e2c85ec0e637', 'e0be4ccde587e4502852edfe');
    $client->push()
        ->setPlatform('ios')
        ->addRegistrationId($push_id)
        ->addIosNotification($message, '广西师范学院', 1, $data)
        ->send();
}

//发送通知 - 向 Android 指定的 RegistrationId
function sendNotifyAndroidByPushId($push_id, $message, $data)
{
    if (empty($push_id) || empty($message)) {
        return;
    }

    require_once("JPush/JPush.php");
    $client = new \JPush('5f1fab3af9a9e2c85ec0e637', 'e0be4ccde587e4502852edfe');
    $client->push()
        ->setPlatform('android')
        ->addRegistrationId($push_id)
        ->addAndroidNotification($message, '广西师范学院', 1, $data)
        ->send();
}