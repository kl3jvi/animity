import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { VideoplayerPage } from './videoplayer.page';

describe('VideoplayerPage', () => {
  let component: VideoplayerPage;
  let fixture: ComponentFixture<VideoplayerPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VideoplayerPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(VideoplayerPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
