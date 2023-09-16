import React from "react";
import { Form, Button, Input, Space, Checkbox, message } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { login, register } from "../utils";


class LoginPage extends React.Component {
  formRef = React.createRef();//通过ref可以拿到form的instance，从而进行一些操作
  //可以理解为通过绑定ref可以获取对应组建里面的内容object
  state = {
    asHost: false,
    loading: false,
  };


  onFinish = () => {//登陆界面简化了，只做了一张表，上面有两个button，但是一张表只能有一个submit，所以需要先用ref，然后通过绑定后拿到class的内容
    console.log("finish form");
  };


  handleLogin = async () => {//async和await需要成对出现
    //async出现在函数前面，await出现在函数里面
    //是一个syntax sugar
    //用了await之后 fetch().then(resp => {})就可以写成 const resp = await fetch()；
    //将函数里套函数的情况扁平化 -- then()里套了一个回调函数
    //method().then(resp => console.log(resp))
    //就可以写成
    //const resp = method();
    //console.log(resp);
    const formInstance = this.formRef.current;


    try {
      await formInstance.validateFields();
    } catch (error) {
      return;
    }


    this.setState({
      loading: true,
    });


    try {
      const { asHost } = this.state;
      const resp = await login(formInstance.getFieldsValue(true), asHost);
      this.props.handleLoginSuccess(resp.token, asHost);
    } catch (error) {
      message.error(error.message);
    } finally {
      this.setState({
        loading: false,
      });
    }
  };


  handleRegister = async () => {
    const formInstance = this.formRef.current;


    try {
      await formInstance.validateFields();
    } catch (error) {
      return;
    }


    this.setState({
      loading: true,
    });


    try {
      await register(formInstance.getFieldsValue(true), this.state.asHost);
      message.success("Register Successfully");
    } catch (error) {
      message.error(error.message);
    } finally {
      this.setState({
        loading: false,
      });
    }
  };


  handleCheckboxOnChange = (e) => {
    this.setState({
      asHost: e.target.checked,
    });
  };


  render() {
    return (
      <div style={{ width: 500, margin: "20px auto" }}>
        <Form ref={this.formRef} onFinish={this.onFinish}>
          <Form.Item
            name="username"
            rules={[
              {
                required: true,
                message: "Please input your Username!",
              },
            ]}
          >
            <Input
              disabled={this.state.loading}
              prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Username"
            />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[
              {
                required: true,
                message: "Please input your Password!",
              },
            ]}
          >
            <Input.Password
              disabled={this.state.loading}
              placeholder="Password"
            />
          </Form.Item>
        </Form>
        <Space>
          <Checkbox
            disabled={this.state.loading}
            checked={this.state.asHost}
            onChange={this.handleCheckboxOnChange}
          >
            As Host
          </Checkbox>
          <Button
            onClick={this.handleLogin}
            disabled={this.state.loading}
            shape="round"
            type="primary"
          >
            Log in
          </Button>
          <Button
            onClick={this.handleRegister}
            disabled={this.state.loading}
            shape="round"
            type="primary"
          >
            Register
          </Button>
        </Space>
      </div>
    );
  }
}


export default LoginPage;

// An async function is a special type of function that always returns a promise. 
// t allows you to use the await keyword inside the function to pause its execution until a promise is resolved.
// This makes it easier to work with asynchronous code as if it were synchronous.
// he await keyword is used inside an async function to pause its execution until the promise passed to it is resolved. It can only be used inside an async function.

// async function fetchAndLogData() {
//   const data = await fetchData(); // Wait for fetchData to complete
//   console.log(data);
// }