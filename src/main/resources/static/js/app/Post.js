class Post {
    constructor() {
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
    };
    removeAttachment = (e) => {
        let attachmentFile = e.target.parentNode;
        this.deleteFileIds.push(Number(e.target.getAttribute("data-id")));
        attachmentFile.remove();
        this.deleteFileIdsInput.value = this.deleteFileIds;
        console.log(this.deleteFileIdsInput.value)
    }
}
