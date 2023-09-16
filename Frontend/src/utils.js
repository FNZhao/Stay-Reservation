//前后端通信的模块

  const domain = "https://vibrant-reach-365719.ue.r.appspot.com";//后端的url

  export const login = (credential, asHost) => {
    const loginUrl = `${domain}/authenticate/${asHost ? "host" : "guest"}`;
    return fetch(loginUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(credential),
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to log in");
      }
  
  
      return response.json();
    });
  };
  
  
  export const register = (credential, asHost) => {
    const registerUrl = `${domain}/register/${asHost ? "host" : "guest"}`;
    return fetch(registerUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(credential),
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to register");
      }
    });
  };
  
  
  export const getReservations = () => {
    const authToken = localStorage.getItem("authToken");
    const listReservationsUrl = `${domain}/reservations`;
  
  
    return fetch(listReservationsUrl, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to get reservation list");
      }
  
  
      return response.json();
    });
  };
  
  
  export const getStaysByHost = () => {
    const authToken = localStorage.getItem("authToken");
    const listStaysUrl = `${domain}/stays/`;
  
  
    return fetch(listStaysUrl, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to get stay list");
      }
  
  
      return response.json();
    });
  };
  
  
  export const searchStays = (query) => {
    const authToken = localStorage.getItem("authToken");
    const searchStaysUrl = new URL(`${domain}/search/`);
    searchStaysUrl.searchParams.append("guest_number", query.guest_number);
    searchStaysUrl.searchParams.append(
      "checkin_date",
      query.checkin_date.format("YYYY-MM-DD")
    );
    searchStaysUrl.searchParams.append(
      "checkout_date",
      query.checkout_date.format("YYYY-MM-DD")
    );
    searchStaysUrl.searchParams.append("lat", 37);
    searchStaysUrl.searchParams.append("lon", -122);
  
  
    return fetch(searchStaysUrl, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to search stays");
      }
  
  
      return response.json();
    });
  };
  
  
  export const deleteStay = (stayId) => {
    const authToken = localStorage.getItem("authToken");
    const deleteStayUrl = `${domain}/stays/${stayId}`;//stayId是route parameter，放到了url路径里
  
  
    return fetch(deleteStayUrl, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to delete stay");
      }
    });
  };
  
  
  export const bookStay = (data) => {
    const authToken = localStorage.getItem("authToken");
    const bookStayUrl = `${domain}/reservations`;
  
  
    return fetch(bookStayUrl, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to book reservation");
      }
    });
  };
  
  
  export const cancelReservation = (reservationId) => {
    const authToken = localStorage.getItem("authToken");
    const cancelReservationUrl = `${domain}/reservations/${reservationId}`;
  
  
    return fetch(cancelReservationUrl, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to cancel reservation");
      }
    });
  };
  
  
  export const getReservationsByStay = (stayId) => {
    const authToken = localStorage.getItem("authToken");
    const getReservationByStayUrl = `${domain}/stays/reservations/${stayId}`;
  
  
    return fetch(getReservationByStayUrl, {
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to get reservations by stay");
      }
  
  
      return response.json();
    });
  };
  
  
  export const uploadStay = (data) => {//这个api要传的信息里会包含文件内容，所以信息不能放到url里，所以有一部分会在UI层用浏览器自带的方法实现,并且也不是用json传输
    const authToken = localStorage.getItem("authToken");
    const uploadStayUrl = `${domain}/stays`;
  
  
    return fetch(uploadStayUrl, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${authToken}`,
      },
      body: data,
    }).then((response) => {
      if (response.status !== 200) {
        throw Error("Fail to upload stay");
      }
    });
  };
  

//这个函数后来要用在UI层
// export const login = (credential, asHost) => { //export使得它右边的变量可以被src下面的其他文件使用
//     //这两个参数第一个是登陆时候的用户密码，第二个是登陆的身份
//     const loginUrl = `${domain}/authenticate/${asHost ? "host" : "guest"}`;

    
//     // const requestStatus = fetch(loginUrl, {
//     //     method: "POST",
//     //     headers: {
//     //         "Content-Type": "application/json", 
//     //     },
//     //     body: JSON.stringify(),
//     // });

//     // requestStatus.then((response) => {

//     // })

//     //调用了fetch函数，前端就会发一个http请求
//     //fetch接收了两个参数，第一个是请求的api的url，指这个请求发到哪，是个string
//     //第二个是一个javascript object，是请求的配置信息metadata
//     //fetch函数的返回值是一个promise，用于tracking request status
//     //status只有三种状态，processing, succeed, fail
//     return fetch(loginUrl, {
//         method: "POST",
//         headers: {//request headers, 给后端看的
//             "Content-Type": "application/json", //告诉后端 前端的数据是以json的格式发送的
//         },
//         body: JSON.stringify(credential), //把object压成一个string，后端知道这是一个json string
//     }).then((response) => {//then是个callback，response是Promise里的resolved value
//         if (response.status !== 200) {
//             throw Error("Fail to log in");
//         }

//         return response.json;
//     });
// };

// export const searchStays = (query) => {//规定为只能guest用,是需要身份验证的api，登陆后，后端会给前端一个token，前端会存到local storage里，持续时间比较久
//     const authToken = localStorage.getItem("authToken");//user的信息可以从token里拿到
//     //这个url里的query string(?a=1&b=2&c=3)比较多，如果用template string的话很容易写错，所以推荐用浏览器自带的URL class
//     const searchStaysUrl = new URL (`${domain}/search`);
//     searchStaysUrl.searchParams.append(
//         "guest_number", query.guest_number
//     );
//     searchStaysUrl.searchParams.append(
//         "checkin_date",
//         query.checkin_date.format("YYYY-MM-DD")
//     );
//     searchStaysUrl.searchParams.append("lat", 37);
//     searchStaysUrl.searchParams.append("lon", -122);

//     return fetch(searchStaysUrl, {
//         headers: {
//             Authorization: `Bearer ${authToken}`,//Bearer是token的类型
//         },
//     }).then((response) => {
//         if (response.status !== 200) {
//             throw Error("Fail to search stays");
//         }

//         return response.json();
//     });
// };