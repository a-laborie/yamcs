import { ChangeDetectionStrategy, Component, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommandHistoryEntry, Instance } from '@yamcs/client';
import { BehaviorSubject, Subscription } from 'rxjs';
import { filter } from 'rxjs/operators';
import { YamcsService } from '../../core/services/YamcsService';
import { printCommandId } from '../../shared/utils';
import { CommandHistoryRecord } from '../command-history/CommandHistoryRecord';

@Component({
  templateUrl: './CommandReportPage.html',
  styleUrls: ['./CommandReportPage.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class CommandReportPage implements OnDestroy {

  instance: Instance;

  private commandSubscription: Subscription;
  command$ = new BehaviorSubject<CommandHistoryRecord | null>(null);

  constructor(
    route: ActivatedRoute,
    private yamcs: YamcsService,
  ) {
    const id = route.snapshot.paramMap.get('commandId')!;

    this.instance = yamcs.getInstance();

    yamcs.getInstanceClient()!.getCommandUpdates({
      ignorePastCommands: false,
    }).then(response => {

      this.commandSubscription = response.command$.pipe(
        filter(entry => printCommandId(entry.commandId) === id),
      ).subscribe(entry => this.mergeEntry(entry));

      yamcs.getInstanceClient()!.getCommandHistoryEntry(id).then(entry => {
        this.mergeEntry(entry);
      });
    });
  }

  private mergeEntry(entry: CommandHistoryEntry) {
    const rec = this.command$.value;
    if (rec) {
      const mergedRec = rec.mergeEntry(entry);
      this.command$.next(mergedRec);
    } else {
      this.command$.next(new CommandHistoryRecord(entry));
    }
  }

  ngOnDestroy() {
    if (this.commandSubscription) {
      this.commandSubscription.unsubscribe();
      const client = this.yamcs.getInstanceClient();
      if (client) {
        client.unsubscribeCommandUpdates();
      }
    }
  }
}