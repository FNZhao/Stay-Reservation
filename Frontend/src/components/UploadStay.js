import React from "react";
import { Form, Input, InputNumber, Button, message } from "antd";
import { uploadStay } from "../utils";


const layout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 },
};


class UploadStay extends React.Component {
  state = {
    loading: false,
  };


  fileInputRef = React.createRef();


  handleSubmit = async (values) => {
    const formData = new FormData();//FormData用于发送前端请求，因为json无法传输文件（因为需要上传图片），所以需要用浏览器自带的FormData来传输（Postman里可以看）
    const { files } = this.fileInputRef.current;//antd Form submit时可以获取其他部分的信息但是无法获取<input>里的，所以需要用ref来获取信息


    if (files.length > 5) {
      message.error("You can at most upload 5 pictures.");
      return;
    }


    for (let i = 0; i < files.length; i++) {
      formData.append("images", files[i]);
    }

    //这里的第二个参数都要和Form组件里定义的名字一致
    formData.append("name", values.name);
    formData.append("address", values.address);
    formData.append("description", values.description);
    formData.append("guest_number", values.guest_number);


    this.setState({
      loading: true,
    });
    try {
      await uploadStay(formData);
      message.success("upload successfully");
    } catch (error) {
      message.error(error.message);
    } finally {
      this.setState({
        loading: false,
      });
    }
  };


  render() {
    return (
      <Form
        {...layout}
        name="nest-messages"
        onFinish={this.handleSubmit}
        style={{ maxWidth: 1000, margin: "auto" }}
      >
        <Form.Item name="name" label="Name" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="address" label="Address" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item
          name="description"
          label="Description"
          rules={[{ required: true }]}
        >
          <Input.TextArea autoSize={{ minRows: 2, maxRows: 6 }} />
        </Form.Item>
        <Form.Item
          name="guest_number"
          label="Guest Number"
          rules={[{ required: true, type: "number", min: 1 }]}
        >
          <InputNumber />
        </Form.Item>
        <Form.Item name="picture" label="Picture" rules={[{ required: true }]}>
          <input
            type="file"
            accept="image/png, image/jpeg"
            ref={this.fileInputRef}
            multiple={true}
          />
        </Form.Item>
        <Form.Item wrapperCol={{ ...layout.wrapperCol, offset: 8 }}>
          <Button type="primary" htmlType="submit" loading={this.state.loading}>
            Submit
          </Button>
        </Form.Item>
      </Form>
    );
  }
}
//antd的Input无法上传文件，所以用的浏览器自带的<input>组件


export default UploadStay;
