import {
  message,
  Tabs,
  List,
  Card,
  Image,
  Carousel,
  Button,
  Tooltip,
  Space,
  Modal,
} from "antd";
import {
  LeftCircleFilled,
  RightCircleFilled,
  InfoCircleOutlined,
} from "@ant-design/icons";
import Text from "antd/lib/typography/Text";
import React from "react";
import { deleteStay, getStaysByHost, getReservationsByStay } from "../utils";
import UploadStay from "./UploadStay";


const { TabPane } = Tabs;

class ReservationList extends React.Component {
  state = {
    loading: false,
    reservations: [],
  };


  componentDidMount() {
    this.loadData();
  }


  loadData = async () => {
    this.setState({
      loading: true,
    });


    try {
      const resp = await getReservationsByStay(this.props.stayId);
      this.setState({
        reservations: resp,
      });
    } catch (error) {
      message.error(error.message);
    } finally {
      this.setState({
        loading: false,
      });
    }
  };


  render() {
    const { loading, reservations } = this.state;

    return (
      <List
        loading={loading}
        dataSource={reservations}
        renderItem={(item) => (
          <List.Item>
            <List.Item.Meta
              title={<Text>Guest Name: {item.guest.username}</Text>}
              description={
                <>
                  <Text>Checkin Date: {item.checkin_date}</Text>
                  <br />
                  <Text>Checkout Date: {item.checkout_date}</Text>
                </>
              }
            />
          </List.Item>
        )}
      />
    );
  }
}

class ViewReservationsButton extends React.Component {
  state = {
    modalVisible: false,
  };


  openModal = () => {
    this.setState({
      modalVisible: true,
    });
  };


  handleCancel = () => {
    this.setState({
      modalVisible: false,
    });
  };


  render() {
    const { stay } = this.props;
    const { modalVisible } = this.state;


    const modalTitle = `Reservations of ${stay.name}`;


    return (
      <>
        <Button onClick={this.openModal} shape="round">
          View Reservations
        </Button>
        {modalVisible && (
          <Modal
            title={modalTitle}
            centered={true}
            visible={modalVisible}
            closable={false}
            footer={null}
            onCancel={this.handleCancel}
            destroyOnClose={true}
          >
            <ReservationList stayId={stay.id} />
          </Modal>
        )}
      </>
    );
  }
}

export class RemoveStayButton extends React.Component {
  state = {
    loading: false
  }

  handleRemoveStay = async () => {
    const {stay, onRemoveSuccess} = this.props;
    this.setState({
      loading: true
    });

    try {
      await deleteStay(stay.id);
      onRemoveSuccess();
    } catch (error) {
      message.error(error.message);
    } finally {
      this.setState({
        loading: false
      });
    }
  }

  render() {
    return(
      <Button 
        loading={this.state.loading}
        onClick={this.handleRemoveStay}
        danger={true}//按钮变为红色
        shape="round"  
        type="primary"
      >Remove Stay</Button>
    );
  }
}

class HostHomePage extends React.Component {
  render() {
    return (
      <Tabs defaultActiveKey="1" destroyInactiveTabPane={true}>
        <TabPane tab="My Stays" key="1">
          <MyStays />
        </TabPane>
        <TabPane tab="Upload Stay" key="2">
          <UploadStay />
        </TabPane>
      </Tabs>
    );
  }
}

export class StayDetailInfoButton extends React.Component {
  state = {
    modalVisible: false,
  }

  openModal = () => {
    this.setState({
      modalVisible: true
    });
  }

  closeModal = () => {
    this.setState({
      modalVisible: false
    });
  }

  render() {
    const { stay } = this.props;
    const { name, description, address, guest_number } = stay;
    const { modalVisible } = this.state;

    return (//React规定如果最上面的一层有两个以上的平行组件，则需要在外面套一层空tag，用其他的tag类似div也可以，但是会额外增加一个组件，可能会出问题
      <>
        <Tooltip title="View Stay Details"
          //tooltip包裹住哪个component，则当鼠标移动到其上时会弹出文字
        >
          <Button 
          //我们希望这是一个没有文字的button，所以是自己封口的，不写第二个button tag
          onClick={this.openModal}
          style={{ border: "none" }}
          size="large"
          icon={<InfoCircleOutlined />}
          />
        </Tooltip>
        {modalVisible && (
        <Modal 
          title={name}
          centered={true}//强迫modal上下居中，水平是默认居中
          visible={this.state.modalVisible}
          closable={false}//是否显示modal的X
          footer={null}
          onCancle={this.closeModal} //这个是点击modal界面的X时所触发的方法
        >
          <Space direction="vertical"
          //space的作用是把里面的内容做一个均匀分布
          >
              <Text strong={true}//strong是指粗体字
              >Description</Text>
              <Text type="secondary">{description}</Text>
              <Text strong={true}>Address</Text>
              <Text type="secondary">{address}</Text>
              <Text strong={true}>Guest Number</Text>
              <Text type="secondary">{guest_number}</Text>
            </Space>
        </Modal>
        )}
      </>
    );
  }
}

class MyStays extends React.Component {
  state = {
    loading: false,
    data: [],
  };


  componentDidMount() {
    this.loadData();
  }


  loadData = async () => {
    this.setState({
      loading: true,
    });


    try {
      const resp = await getStaysByHost();
      this.setState({
        data: resp,
      });
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
      <List
        loading={this.state.loading}
        grid={{
          gutter: 16,
          xs: 1,
          sm: 3,
          md: 3,
          lg: 3,
          xl: 4,
          xxl: 4,
        }}
        dataSource={this.state.data}//dataSource一定是一个数组, 之后renderItem则会遍历每一个元素然后进行操作转换成jsx
        renderItem={(item) => (//item来自于datasource
          <List.Item>
            <Card
              key={item.id}
              title={
                <div style={{ display: "flex", alignItems: "center" }}>
                  <Text ellipsis={true} style={{ maxWidth: 150 }}>
                    {item.name}
                  </Text>
                  <StayDetailInfoButton stay={item} />
                </div>
              }
              actions={[<ViewReservationsButton stay={item} />]}//action接受的是一个数组类型
              extra={<RemoveStayButton stay={item} onRemoveSuccess={this.loadData}/>}
            >
                <Carousel
                  dots={false}
                  arrows={true}
                  prevArrow={<LeftCircleFilled />}
                  nextArrow={<RightCircleFilled />}
                >
                  {item.images.map((image, index) => (
                    <div key={index}>
                      <Image src={image.url} width="100%" />
                    </div>
                  ))}
                </Carousel>
            </Card>
          </List.Item>
        )}
      />
    );
  }
}


export default HostHomePage;
