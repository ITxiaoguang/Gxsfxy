<?php

namespace Mobile\Model;

use Think\Model;

class UserModel extends Model
{

    protected $tableName = 'user';

    //公共

    public function getRowById($id)
    {
        $where = array();
        $where['user_id'] = $id;
        $data = $this->where($where)->select();
        return $data[0];
    }

    public function updateById($id, $data)
    {
        $where = array();
        $where['user_id'] = $id;
        return $this->where($where)->save($data);
    }

    public function getFiledById($id, $filed)
    {
        $where = array();
        $where['user_id'] = $id;
        return $this->where($where)->getField($filed);
    }

    public function getFiledByKV($key, $value, $filed)
    {
        $where = array();
        $where[$key] = $value;
        return $this->where($where)->getField($filed);
    }

    public function updateByIdWithKV($id, $key, $value)
    {
        $data = array();
        $data[$key] = $value;
        return $this->updateById($id, $data);
    }

    public function insert($mobile, $pass, $stu_id, $stu_pass, $college, $professional, $class, $nick_name, $true_name, $card, $birthday, $gender, $reg_time, $device_name, $device_system, $app_version)
    {
        $data = array();
        $data['user_power'] = '用户';
        $data['user_mobile'] = $mobile;
        $data['user_pass'] = $pass;
        $data['stu_id'] = $stu_id;
        $data['stu_pass'] = $stu_pass;
        $data['user_college'] = $college;
        $data['user_professional'] = $professional;
        $data['user_class'] = $class;
        $data['nick_name'] = $nick_name;
        $data['true_name'] = $true_name;
        $data['user_card'] = $card;
        $data['user_birthday'] = $birthday;
        $data['user_gender'] = $gender;
        $data['user_points'] = '100';
        $data['email_bind'] = '0';
        $data['user_follow'] = '0';
        $data['follow_mine'] = '0';
        $data['user_visitor'] = '0';
        $data['reg_time'] = $reg_time;
        $data['login_state'] = '0';
        $data['login_num'] = '0';
        $data['device_name'] = $device_name;
        $data['device_system'] = $device_system;
        $data['app_version'] = $app_version;
        $data['is_close'] = '0';
        return $this->add($data);
    }

    //自定义

    public function getBaseInfo($id)
    {
        $where = array();
        $where['user_id'] = $id;
        $filed = 'user_id,user_mobile,user_college,user_professional,user_class,nick_name,user_province,user_city,user_area,user_avatar,user_qq,user_sign,user_gender,user_points,user_follow,follow_mine,user_visitor,reg_time';
        return $this->where($where)->field($filed)->find();
    }

    public function updateLoginIp($id)
    {
        $data = array();
        $data['login_last_ip'] = $this->getFiledById($id, 'login_ip');
        $data['login_ip'] = get_client_ip();
        return $this->updateById($id, $data);
    }

    public function updateLoginNum($id)
    {
        $num = $this->getFiledById($id, 'login_num');
        $num = intval($num) + 1;
        return $this->updateByIdWithKV($id, 'login_num', $num);
    }

    public function dayRegNumber($day)
    {
        $where = array();
        $day = '%' . $day . '%';
        $where['reg_time'] = array('like', $day);
        return sizeof($this->where($where)->select());
    }

    public function dayLoginNumber($day)
    {
        $where = array();
        $day = '%' . $day . '%';
        $where['login_time'] = array('like', $day);
        return sizeof($this->where($where)->select());
    }

    public function addFollowNumber($id)
    {
        $follow = $this->getFiledById($id, 'user_follow');
        $follow = intval($follow) + 1;
        return $this->updateByIdWithKV($id, 'user_follow', $follow);
    }

    public function subFollowNumber($id)
    {
        $follow = $this->getFiledById($id, 'user_follow');
        $follow = intval($follow) - 1;
        return $this->updateByIdWithKV($id, 'user_follow', $follow);
    }

    public function addVisitorNumber($id)
    {
        $visitor = $this->getFiledById($id, 'user_visitor');
        $visitor = intval($visitor) + 1;
        return $this->updateByIdWithKV($id, 'user_visitor', $visitor);
    }

    public function subVisitorNumber($id)
    {
        $visitor = $this->getFiledById($id, 'user_visitor');
        $visitor = intval($visitor) - 1;
        return $this->updateByIdWithKV($id, 'user_visitor', $visitor);
    }

    public function addPoints($id, $number)
    {
        $points = $this->getFiledById($id, 'user_points');
        $points = intval($points) + intval($number);
        return $this->updateByIdWithKV($id, 'user_points', $points);
    }

    public function subPoints($id, $number)
    {
        $points = $this->getFiledById($id, 'user_points');
        $points = intval($points) - intval($number);
        return $this->updateByIdWithKV($id, 'user_points', $points);
    }

    public function addFollowMineNumber($id)
    {
        $follow = $this->getFiledById($id, 'follow_mine');
        $follow = intval($follow) + 1;
        return $this->updateByIdWithKV($id, 'follow_mine', $follow);
    }

    public function subFollowMineNumber($id)
    {
        $follow = $this->getFiledById($id, 'follow_mine');
        $follow = intval($follow) - 1;
        return $this->updateByIdWithKV($id, 'follow_mine', $follow);
    }

    public function updateLoginTime($id, $time)
    {
        $data = array();
        $data['login_last_time'] = $this->getFiledById($id, 'login_time');
        $data['login_time'] = $time;
        return $this->updateById($id, $data);
    }

}