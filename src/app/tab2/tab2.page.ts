import { Component } from '@angular/core';

@Component({
  selector: 'app-tab2',
  templateUrl: 'tab2.page.html',
  styleUrls: ['tab2.page.scss']
})
export class Tab2Page {

  constructor() {
    const isIFrame = (input: HTMLElement | null): input is HTMLIFrameElement =>
    input !== null && input.tagName === 'IFRAME';

function ngAfterViewInit() {
    let frame = document.getElementById('vid');
    if (isIFrame(frame) && frame.contentWindow) {
        frame.contentWindow.postMessage({
          orientation: window.orientation
      }, 'https://cloud9.to/embed/hg-WKzbo7DSP');
    }
  }
  }



  
}
