let socket;
let userId, recipientId;

function connect() {
    userId = document.getElementById('userId').value;
    const chattingStatus = document.getElementById('chattingStatus').value;
    const serverPort = $('#serverPort').val();
    const serverIp = $('#serverIp').val();
//	socket = new WebSocket('ws://localhost:' + serverPort + '/chat?userId=' + userId + '&status=' + chattingStatus);
    socket = new WebSocket(`ws://${serverIp}:${serverPort}/chat?userId=${userId}&status=${chattingStatus}`);

    socket.onopen = () => {
        console.log('Connected as ' + userId);
        $('#statusIcon').css({color: 'green', fontWeight: 'bold'});
    }
    socket.onmessage = async(event) => {
        console.log('Message from server: ' + event.data);
        setTimeout(async () => {
            await fetchChatterList();
        }, 200);
    }
    socket.onclose = () => {
        console.log('Disconnected from the server');
        $('#statusIcon').css({color: 'red', fontWeight: 'bold'});
    }
}

async function fetchChatterList() {
    const userId = $('#userId').val();
    try {
        const response = await fetch(`/chatting/getChatterList?userId=${userId}`);
        if (response.ok) {
            const chatterList = await response.json();
            updateChatterList(chatterList);
        }
    } catch (error) {
        console.error('Failed to fetch chatter list:', error);
    }
}

function updateChatterList(chatterList) {
    const tableBody = document.querySelector('.table > tbody');
    tableBody.innerHTML = ''; // 기존 내용을 초기화

    chatterList.forEach(chatter => {
        const row = document.createElement('tr');

        row.innerHTML = `
            <td style="text-align: center;">
                <img src="${chatter.friendProfileUrl}" alt="${chatter.friendUname}" width="40" style="border-radius: 50%">
            </td>
            <td>
                <a href="/chatting/chat/${chatter.friendUid}">
                    <span style="font-weight: bold; font-size: 0.8rem">${chatter.friendUname}</span>
                </a><br>
                <span style="font-size: 0.8rem;">${chatter.message}</span>
            </td>
            <td style="text-align: center;">
                <span style="font-size: 0.8rem;">
                    ${chatter.timeStr}
                    ${chatter.newCount !== 0 ? `<br><span class="new-count">${chatter.newCount}</span>` : ''}
                </span>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function disconnect() {
    if (socket && socket.readyState === socket.OPEN) {
        socket.close();
        console.log('Disconnected from the server by ' + recipientId);
    } else {
        alert('WebSocket is not connected.');
    }
}

function handlePopover() {
    // Popover 초기화
    const popoverTrigger = document.getElementById('addFriendPopover');
    const popover = new bootstrap.Popover(popoverTrigger, {
        trigger: 'manual', html: true, placement: 'bottom', title: '친구 추가',
        content: document.getElementById('mypopover-content')
    });

    // 팝오버 열기
    popoverTrigger.addEventListener('click', function(event) {
        event.stopPropagation(); // 이벤트 버블링 방지
        if (popoverTrigger.getAttribute('aria-expanded') === 'true') {
            popover.hide();
        } else {
            popover.show();
        }
    });

    // Popover 열렸을 때 버튼 클릭 이벤트 바인딩
    document.addEventListener('shown.bs.popover', function () {
        const addButton = document.getElementById('addFriendButton');
        const closeButton = document.getElementById('closePopoverButton');
        if (addButton) {
            addButton.addEventListener('click', function () {
                const friendUid = document.getElementById('friendUid').value.trim();
                if (friendUid !== '') {
                    if (confirm(`친구 ${friendUid}를 추가합니다.`)) {
                        // 서버에 AJAX 요청 예시
                        $.post('/chatting/addFriend', { friendUid: friendUid }, function (response) {
                            console.log('친구 추가 성공:', response);
                            fetchChatterList();
                        });
                    }
                    popover.hide();
                } else {
                    alert('친구 ID를 입력하세요.');
                }
            });
        }
        if (closeButton) {
            closeButton.onclick = function () {
                popover.hide();
            };
        }
    });

    // 팝오버 외부를 클릭하면 닫기
    document.addEventListener('click', function (event) {
        const popoverElement = document.querySelector('.popover');
        if (popoverElement && !popoverElement.contains(event.target) && event.target !== popoverTrigger) {
            popover.hide();
        }
    });
}