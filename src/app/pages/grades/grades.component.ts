import { Component } from '@angular/core';
import { ItemInfoCardComponent } from '../../shared/item-info-card/item-info-card.component';

@Component({
  selector: 'app-grades',
  standalone: true,
  imports: [ItemInfoCardComponent],
  templateUrl: './grades.component.html',
  styleUrl: './grades.component.scss'
})
export class GradesComponent {

}
