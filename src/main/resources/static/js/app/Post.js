class Post {
    constructor(postId) {
        this.deleteFileIds = [];
        this.deleteFileIdsInput = document.getElementById("delete-file-ids-input");
        this.fileDeleteBtns = document.getElementsByClassName("file-delete-btn");
        this.fileInput = document.getElementById("file-input");
        if (this.fileDeleteBtns) {
            this.fileNum = this.fileDeleteBtns.length;
            for (let i = 0; i < this.fileNum; i++) {
                this.fileDeleteBtns[i].addEventListener("click", this.removeAttachment)
            }
        }
        if (this.fileInput) {
            this.fileInput.addEventListener("click", (e) => {
                if (this.fileNum !== 0) {
                    e.preventDefault();
                    alert("첨부 파일은 최대 1개 업로드할 수 있습니다.");
                }
            })
        }
    };
    removeAttachment = (e) => {
        let attachmentFile = e.target.parentNode;
        this.deleteFileIds.push(Number(attachmentFile.getAttribute("data-id")));
        attachmentFile.remove();
        this.deleteFileIdsInput.value = this.deleteFileIds;
        this.fileNum--;
    }
}