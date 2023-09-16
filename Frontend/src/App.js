import { Layout, Dropdown, Menu, Button } from "antd";
import { UserOutlined } from "@ant-design/icons";
import React from "react";
import LoginPage from "./components/LoginPage";
import HostHomePage from "./components/HostHomePage";
import GuestHomePage from "./components/GuestHomePage";


const { Header, Content } = Layout;//解构 deconstruct


class App extends React.Component {
  state = {
    authed: false,
    asHost: false,
  };


  componentDidMount() {
    const authToken = localStorage.getItem("authToken");//localStorage是在浏览器里的variable，和fetch一样，都是global的. 浏览器里所有global的东西都在window里
    const asHost = localStorage.getItem("asHost") === "true";//localStorage大小与内存成正比，cookie最大2M。cookie时长默认比localStorage短
    //console.log(localStorage);
    this.setState({
      authed: authToken !== null,//判断语句，如果不是null则为true，如果是null则为false//此方法并没有考虑到token过期的情况
      asHost,
    });
  }


  handleLoginSuccess = (token, asHost) => {//登陆成功之后执行//token失效后会返回403
    localStorage.setItem("authToken", token);//后端返回的是token，这里以authToken的名字存到了localStorage里
    localStorage.setItem("asHost", asHost);
    this.setState({
      authed: true,
      asHost,
    });
  };


  handleLogOut = () => {
    localStorage.removeItem("authToken");//登出时要清除相应数据
    localStorage.removeItem("asHost");
    this.setState({
      authed: false,
    });
  };


  renderContent = () => {
    if (!this.state.authed) {
      return <LoginPage handleLoginSuccess={this.handleLoginSuccess}/>;//也可以写成<LoginPage></LoginPage>
    }                   //这里传入的事函数本身而不是函数调用的结果，所以函数后面不要加小括号

    if (this.state.asHost) {
      return <HostHomePage />;
    }


    return <GuestHomePage />;
  };


  userMenu = (
    <Menu>
      <Menu.Item key="logout" onClick={this.handleLogOut}>
        Log Out
      </Menu.Item>
    </Menu>
  );


  render() {
    return (
      <Layout style={{ height: "100vh" }}>
        <Header style={{ display: "flex", justifyContent: "space-between" }}>
          <div style={{ fontSize: 16, fontWeight: 600, color: "white" }}>
            Stays Booking
          </div>
          {this.state.authed && (
            <div>
              <Dropdown trigger="click" overlay={this.userMenu}>
                <Button icon={<UserOutlined />} shape="circle" />
              </Dropdown>
            </div>
          )}
        </Header>
        <Content
          style={{ height: "calc(100% - 64px)", margin: 20, overflow: "auto" }}
        >
          {this.renderContent()}
        </Content>
      </Layout>
    );
  }
}

export default App;
//App.js是项目中最顶层的react component

//node.js是一个environment，用于运行javascript，使得开发过程中在浏览器外也能运行js
//运行代码的过程为：浏览器从服务器获取文件然后运行，在本地的话浏览器和服务器是一台机器
//后端是运行后端代码，前端服务器是储存前端代码，然后运行在想要访问此网站的浏览器上
//npm会管理package.json里的dependencies
//package-lock.json文件里是实际安装上的dependency，虽然项目只依赖一小部分dependencies，但是这些dependencies同样依赖于其他的dependencies
//安装过程就是把这些文件拷贝到node_modules里

//Component尖括号后面的参数叫props
//Tag尖括号后面的参数叫attributes

//在jsx里嵌入普通js代码的符号是{}

